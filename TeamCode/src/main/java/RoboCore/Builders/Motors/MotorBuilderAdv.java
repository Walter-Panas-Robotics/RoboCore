package RoboCore.Builders.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import RoboCore.Controllers.PIDFController;

public class MotorBuilderAdv {
    private ZeroPowerState zeroPowerState;
    private String customName;
    private String hardwareName;
    private MotorType motorType;
    private Brand brand;
    private DcMotorSimple.Direction direction;
    private DcMotor.RunMode runMode;
    private PIDFController pidfController;
    private DcMotor.ZeroPowerBehavior zeroPowerBehavior;
    private DcMotor.RunMode mode;

    public MotorBuilder(String hardwareName) {
        this.hardwareName = hardwareName;

        // Default values if any
        this.direction = DcMotorSimple.Direction.FORWARD;
        this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
        this.mode = DcMotor.RunMode.RUN_USING_ENCODER; // Common default
    }

    public MotorBuilder customName(String customName) {
        this.customName = customName;
        return this;
    }

    public MotorBuilder motorType(MotorType motorType) {
        this.motorType = motorType;
        return this;
    }

    public MotorBuilder brand(Brand brand) {
        this.brand = brand;
        return this;
    }

    public MotorBuilder direction(Direction direction) {
        switch (direction) {
            case FORWARD:
                this.direction = DcMotorSimple.Direction.FORWARD;
                break;
            case REVERSE:
                this.direction = DcMotorSimple.Direction.REVERSE;
                break;
        }
        return this;
    }

    public MotorBuilder zeroPowerState(ZeroPowerState zeroPowerState) {
        switch (zeroPowerState) {
            case COAST:
                this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
                break;
            case BRAKE:
                this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
        }
        return this;
    }

    public MotorBuilder runMode(DcMotor.RunMode runMode) {
        this.runMode = runMode;
        return this;
    }

    public enum MotorLocation {FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT, ARM, INTAKE}

    public enum MotorType {DRIVETRAIN, ARM, INTAKE, SHOOTER, OTHER}

    public enum Brand {REV_ULTRAPLANETARY, GOBILDA_YELLOWJACKET, TETRIX, OTHER}

    public enum Direction {FORWARD, REVERSE}

    public enum ZeroPowerState {COAST, BRAKE}


}
