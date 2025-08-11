package RoboCore;

import static RoboCore.Tools.BooleanTools.exists;

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
import RoboCore.Managers.CommandArchitechture;
import RoboCore.Managers.CommandArchitechture.Command;
import RoboCore.Managers.GamepadManager;
import RoboCore.Managers.IMUManager;
import RoboCore.Managers.TelemetryManager;

public class Robot extends RoboCore {
    private static final GamepadManager gamepadManager = GamepadManager.getInstance();
    private static final Map<String, DcMotorEx> motors = new HashMap<>();
    private static final Map<MotorLocation, DcMotorEx> internal_motors = new HashMap<>();

    public static Telemetry telemetry;
    public static TelemetryManager telemetryManager;
    public static HardwareMap hardwareMap;
    public static IMUManager imuManager;
    public static Map<String, HardwareDevice> hardware_devices = new HashMap<>();
    public static IMU imu;
    private static volatile Robot instance; // Made volatile for thread safety if buildInstance is synchronized
    private static double lastUpdateTime;
    private static OpMode opMode; // Should be initialized by the builder
    private static Method driveMethod;
    private static double wheelDiameter;


    private Robot(Builder builder) {
        opMode = builder.opMode;
        Robot.telemetry = opMode.telemetry;
        Robot.hardwareMap = opMode.hardwareMap;
        wheelDiameter = builder.wheelDiameter;
        driveMethod = builder.driveMethod;
        Robot.telemetryManager = new TelemetryManager(opMode.telemetry, true);

    }

    public static OpMode getOpMode() {
        return opMode;
    }

    public static Method getMethodFromName(Class<?> specifiedClass, String name) {
        try {
            return specifiedClass.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Error getting method: " + name, e);
        }
    }

    private static synchronized Robot buildInstance(Builder builder) {
        if (instance == null) {
            if (builder == null) { // Ensure builder is provided for first initialization
                throw new IllegalStateException("Robot.Builder cannot be null for initial instantiation.");
            }
            instance = new Robot(builder);
        }
        return instance;
    }

    public static Robot getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Robot instance has not been initialized. Call build() on a Robot.Builder first.");
        }
        return instance;
    }

    public static void update() {

        try {
            driveMethod.invoke(opMode, motors.get("frontLeft"), motors.get("backLeft"), motors.get("frontRight"), motors.get("backRight"));
        } catch (Exception e) {
            throw new RuntimeException("Error updating drivetrain", e);
        }

        if (CommandArchitechture.update_commands != null) {
            for (Command command : CommandArchitechture.update_commands) {
                if (command.type == CommandType.UPDATE) {
                    try {
                        command.method.invoke(command.builder);
                    } catch (Exception e) {
                        throw new RuntimeException("Error updating command: " + command.name, e);
                    }
                }
            }
        }

        telemetry.update();
    }

    public static int tick() {
        update();
        long currentTime = System.currentTimeMillis(); // Use long for time
        int delta = (int) (currentTime - lastUpdateTime);
        lastUpdateTime = currentTime;
        return Math.max(delta, 0);
    }

    public static DcMotorEx getMotor(String name) {
        HardwareDevice device = hardware_devices.get(name);

        if (device == null) {
            throw new RuntimeException("Device not found: " + name);
        }

        try {
            if (device instanceof DcMotorEx) {
                motors.put(name, (DcMotorEx) device);
                return (DcMotorEx) device;
            }
        } catch (ClassCastException e) {
            throw new RuntimeException("Device is not a DcMotorEx: " + name, e);
        }
        telemetry.addLine("Error while getting device: " + name);

        throw new MotorNotFound("Error whilst retrieving device: " + name);
    }

    public static Map<String, DcMotorEx> getMotors() {
        return motors;
    }

    enum MotorType {DRIVETRAIN, ARM, INTAKE}

    public static class Builder {

        @NonNull
        private final OpMode opMode;

        double wheelDiameter;

        Method driveMethod;

        public Builder(@NonNull OpMode opMode) {
            this.opMode = opMode;
        }


        public Builder addMotor(String customName, MotorLocation location, DcMotorEx motor) {
            // Now this is fine because DcMotorEx IS A HardwareDevice,
            // and hardware_devices stores HardwareDevice.
            hardware_devices.put(customName, motor);
            internal_motors.put(location, motor);

            System.out.println("Added motor: " + customName + " to static hardware_devices map.");
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

        public Builder addGamepadCommand(boolean gamepad_button, String actionableMethod) {
            GamepadManager.assignCommand(gamepad_button, actionableMethod);
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