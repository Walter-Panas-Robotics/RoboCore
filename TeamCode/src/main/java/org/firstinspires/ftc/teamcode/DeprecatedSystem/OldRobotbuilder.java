package org.firstinspires.ftc.teamcode.DeprecatedSystem;

import android.util.Pair;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;

@Deprecated
public class OldRobotbuilder<O extends OpMode> {
    private final Telemetry telemetry;
    private final HardwareMap hardwareMap;
    private final Class<O> opModeClass;
    private HashMap<String, DcMotor> drivetrainMotors;

    public OldRobotbuilder(Class<O> opModeClass, Telemetry telemetry, HardwareMap hardwareMap) {
        this.opModeClass = opModeClass;
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
    }

    public Class<O> getOpMode() {
        return this.opModeClass;
    }

    public Telemetry getTelemetry() {
        return telemetry;
    }

    public HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    public OldRobotbuilder<O> addMotor(Pair<String, DcMotor> dcMotor, MotorType motorType, MotorLocation motorLocation) {

        switch (motorType) {
            case DRIVETRAIN:
                if (drivetrainMotors == null) {
                    drivetrainMotors = new HashMap<>();
                }
                drivetrainMotors.put(dcMotor.first, dcMotor.second);
                break;
            case ARM:
                break;
            case INTAKE:
                break;
        }

        return this;
    }

    public OldRobot<O> build() {
        return new OldRobot(this);
    }

    public enum MotorType {DRIVETRAIN, ARM, INTAKE}

    public enum MotorLocation {FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT, ARM, INTAKE}
}
