package RoboCore;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import RoboCore.Exceptions.MotorNotFound;
import RoboCore.Managers.CommandArchitecture;
import RoboCore.Managers.CommandArchitecture.Command;
import RoboCore.Managers.GamepadManager;
import RoboCore.Managers.IMUManager;
import RoboCore.Managers.TelemetryManager;

public class Robot extends RoboCore {
    private static final GamepadManager gamepadManager = GamepadManager.getInstance();
    public static final Map<String, HardwareDevice> hardware_devices = new HashMap<>();
    private static final Map<String, DcMotorEx> motors = new HashMap<>();
    private static final Map<MotorLocation, DcMotorEx> internal_motors = new HashMap<>();
    private static volatile boolean hold_lock = false;
    private final OpMode opMode; // Should be initialized by the builder
    public static IMUManager imuManager;
    private final Method driveMethod;
    private final double wheelDiameter;
    public Telemetry telemetry;
    private static volatile Robot instance; // Made volatile for thread safety if buildInstance is synchronized
    private static double lastUpdateTime;
    public TelemetryManager telemetryManager;
    public HardwareMap hardwareMap;
    public IMU imu;


    private Robot(Builder builder) {
        opMode = builder.opMode;
        this.telemetry = opMode.telemetry;
        this.hardwareMap = opMode.hardwareMap;
        wheelDiameter = builder.wheelDiameter;
        driveMethod = builder.driveMethod;
        this.telemetryManager = new TelemetryManager(opMode.telemetry, true);

    }

    public static boolean isHold_lock() {
        return hold_lock;
    }

    public static void setHold_lock(boolean hold_lock) {
        Robot.hold_lock = hold_lock;
    }

    private static synchronized Robot buildInstance(Builder builder) {
        if (instance == null && !hold_lock) {
            if (builder == null) { // Ensure builder is provided for first initialization
                throw new IllegalStateException("Robot.Builder cannot be null for initial instantiation.");
            }
            instance = new Robot(builder);
        }
        return instance;
    }

    public static Method getMethodFromName(Class<?> specifiedClass, String name) {
        try {
            return specifiedClass.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error getting method: " + name, e);
        }
    }

    public static synchronized Robot getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Robot instance has not been initialized. Call build() on a Robot.Builder first.");
        }
        return instance;
    }

    public final OpMode getOpMode() {
        return opMode;
    }

    public void update() {

        try {
            driveMethod.invoke(opMode, motors.get("frontLeft"), motors.get("backLeft"), motors.get("frontRight"), motors.get("backRight"));
        } catch (Exception e) {
            throw new RuntimeException("Error updating drivetrain", e);
        }

        if (CommandArchitecture.update_commands != null) {
            for (Command command : CommandArchitecture.update_commands) {
                if (command.type == CommandType.UPDATE) {
                    try {
                        command.method.invoke(command.builder);
                    } catch (Exception e) {
                        throw new RuntimeException("Error updating command: " + command.name, e);
                    }
                }
            }
        }

        telemetryManager.update();

    }

    public final int tick() {
        update();
        long currentTime = System.currentTimeMillis(); // Use long for time
        int delta = (int) (currentTime - lastUpdateTime);
        lastUpdateTime = currentTime;
        return Math.max(delta, 0);
    }

    public final DcMotorEx getMotor(String name) throws MotorNotFound {
        HardwareDevice device = hardware_devices.get(name);

        if (device == null) {
            throw new MotorNotFound("Motor not found: " + name);
//            throw new RuntimeException("Device not found: " + name);
        }

        try {
            if (device instanceof DcMotorEx) {
                motors.put(name, (DcMotorEx) device);
                return (DcMotorEx) device;
            }
        } catch (ClassCastException e) {
            throw new RuntimeException("Device is not a DcMotorEx: " + name, e);
        }
        telemetryManager.addLine("Error while getting device: " + name);

        throw new MotorNotFound("Error whilst retrieving device: " + name);
    }

    public final Map<String, DcMotorEx> getMotors() {
        return motors;
    }

    public static class Builder {

        @NonNull
        private final OpMode opMode;
        double wheelDiameter;
        Method driveMethod;
        private IMU imu;

        public Builder(@NonNull OpMode opMode) {
            Robot.setHold_lock(true);
            this.opMode = opMode;

        }


        public Builder addMotor(String customName, MotorLocation location, DcMotorEx motor) {
            hardware_devices.put(customName, motor);
            internal_motors.put(location, motor);

            opMode.telemetry.addLine("Added motor: " + customName + " to static hardware_devices map.");
            return this;
        }


        @NonNull
        public Builder addDrivetrain(Class<?> drivetrainClass) {
            try {
                this.driveMethod = drivetrainClass.getMethod("drive", DcMotorEx.class, DcMotorEx.class, DcMotorEx.class, DcMotorEx.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Error adding drivetrain: " + drivetrainClass.getName(), e);
            }
            return this;
        }


        public Builder addArm() {
            return this;
        }


        public Builder addIntake() {
            return this;
        }


        public Builder setWheelDiameter(double diameter, MeasurementUnit unit) {
            this.wheelDiameter = convertToMM(diameter, unit);
            return this;
        }

        public Builder addGamepadCommand(boolean gamepad_button, String actionableMethod, TriggerType triggerType) {
            GamepadManager.assignCommand(gamepad_button, actionableMethod, triggerType);
            return this;
        }

        public Builder addIMU(IMU.Parameters parameters) {
            imuManager = IMUManager.getInstance(parameters);
            imu = imuManager.getIMU();
            return this;
        }


        public Robot build() {

            if (!exists(driveMethod)) {
                try {
                    driveMethod = this.opMode.getClass().getMethod("drive", DcMotorEx.class, DcMotorEx.class, DcMotorEx.class, DcMotorEx.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Error adding drivetrain: " + this.opMode.getClass().getName(), e);
                }
            }
            return Robot.buildInstance(this);

        }
    }
}