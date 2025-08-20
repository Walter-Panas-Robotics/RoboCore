package RoboCore;

import static RoboCore.Config.RoboCore.author;
import static RoboCore.Config.RoboCore.version;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

import RoboCore.Drivetrains.Drivetrain;
import RoboCore.Exceptions.DrivetrainNotFound;
import RoboCore.Exceptions.MotorNotFound;
import RoboCore.Managers.CommandArchitecture;
import RoboCore.Managers.CommandArchitecture.Command;
import RoboCore.Managers.GamepadManager;
import RoboCore.Managers.IMUManager;
import lombok.Getter;

@SuppressWarnings("unused")
@Getter
public class Robot extends RoboCore {
    private static volatile Robot instance;
    private static double lastUpdateTime = 0;
    private final Map<String, HardwareDevice> hardwareDevices = new HashMap<>();
    private final Map<String, DcMotorEx> motors = new HashMap<>();
    private final Map<MotorLocation, DcMotorEx> internalMotors = new HashMap<>();
    private final double maxSpeed;
    private final double controllerDeadzone;
    private final boolean fieldCentric;
    private final double wheelDiameter;
    private final IMUManager imuManager;
    private final GamepadManager gamepadManager;
    private final Drivetrain drivetrain;
    private final OpMode opMode;
    private final HardwareMap hardwareMap;
    private final Telemetry telemetry;
    private final IMU imu;

    private Robot(Builder builder) {
        this.opMode = builder.opMode;
        this.telemetry = opMode.telemetry;
        this.hardwareMap = opMode.hardwareMap;
        this.wheelDiameter = builder.wheelDiameter;
        this.drivetrain = builder.drivetrain;
        this.imuManager = builder.imuManager;
        this.imu = builder.imu;
        this.maxSpeed = builder.maxSpeed;
        this.controllerDeadzone = builder.controllerDeadzone;
        this.fieldCentric = builder.useFieldCentric;
        this.gamepadManager = GamepadManager.getInstance(this.opMode);

        this.internalMotors.putAll(builder.internalMotors);
        this.hardwareDevices.putAll(builder.hardwareDevices);

        drivetrain.init(this);
    }

    public static synchronized Robot buildInstance(Builder builder) {
        if (instance == null) {
            if (builder == null) {
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

    public void update() {
        drivetrain.drive(this, internalMotors);
        if (CommandArchitecture.update_commands != null) {
            for (Command command : CommandArchitecture.update_commands) {
                if (command.type == CommandType.UPDATE) {
                    try {
                        command.method.invoke(command.builder);
                    } catch (Exception e) {
                        telemetry.addLine("Error updating command: " + command.name);
                    }
                }
            }
        }
        telemetry.update();
    }

    @SuppressWarnings("UnusedReturnValue")
    public int tick() {
        update();
        long currentTime = System.currentTimeMillis();
        int delta = (int) (currentTime - lastUpdateTime);
        lastUpdateTime = currentTime;
        return Math.max(delta, 0);
    }

    public DcMotorEx getMotor(String name) throws MotorNotFound {
        HardwareDevice device = hardwareDevices.get(name);
        if (!(device instanceof DcMotorEx)) {
            throw new MotorNotFound("Motor not found or invalid: " + name);
        }
        DcMotorEx motor = (DcMotorEx) device;
        motors.put(name, motor);
        return motor;
    }

    public static class Builder {
        @NonNull
        private final OpMode opMode;
        @NonNull
        private final Drivetrain drivetrain;
        private final Map<MotorLocation, DcMotorEx> internalMotors = new HashMap<>();
        private final Map<String, HardwareDevice> hardwareDevices = new HashMap<>();
        private double wheelDiameter;
        private IMU imu;
        private IMUManager imuManager;
        private double maxSpeed = 100;
        private double controllerDeadzone = 0.1;
        private boolean useFieldCentric = false;

        public Builder(@NonNull OpMode opMode) {
            if (!(opMode instanceof Drivetrain)) {
                throw new DrivetrainNotFound("OpMode must implement Drivetrain interface.");
            }
            this.opMode = opMode;
            this.drivetrain = (Drivetrain) opMode;

            opMode.telemetry.addLine("RoboCore Software \n " + "Version: " + version + "\n Created by: " + author);
            opMode.telemetry.update();
        }

        public Builder addMotor(String customName, MotorLocation location, DcMotorEx motor) {
            internalMotors.put(location, motor);
            hardwareDevices.put(customName, motor);
            opMode.telemetry.addLine("Added motor: " + customName);
            return this;
        }

        public Builder addMotor(MotorLocation location, DcMotorEx motor) {
            internalMotors.put(location, motor);
            return this;
        }

        public Builder addIMU(IMU.Parameters parameters) {
            imuManager = IMUManager.getInstance(parameters);
            imu = imuManager.getIMU();
            return this;
        }

        public Builder setWheelDiameter(double diameter, MeasurementUnit unit) {
            this.wheelDiameter = convertToMM(diameter, unit);
            return this;
        }

        public Builder setMaxSpeed(double maxSpeed) {
            this.maxSpeed = maxSpeed;
            return this;
        }

        public Builder setControllerDeadzone(double deadzone) {
            this.controllerDeadzone = deadzone;
            return this;
        }

        public Builder useFieldCentric(boolean useFieldCentric) {
            this.useFieldCentric = useFieldCentric;
            return this;
        }

        public Builder addGamepadCommand(boolean button, String method, TriggerType triggerType) {
            GamepadManager.assignCommand(button, method, triggerType);
            return this;
        }

        public Builder addGamepadCommand(boolean button, String method) {
            GamepadManager.assignCommand(button, method);
            return this;
        }

        public Robot build() {
            return Robot.buildInstance(this);
        }
    }
}
