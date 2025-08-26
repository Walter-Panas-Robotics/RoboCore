package com.panther_tech.RoboCore.Builders.Motors;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.panther_tech.RoboCore.Config.DcMotorConfig;
import com.panther_tech.RoboCore.Managers.HardwareManager;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class MotorBuilder {

    private final String hardwareName;
    private String customName;
    private DcMotorSimple.Direction direction = DcMotorConfig.defaultDirection;
    private DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotorConfig.defaultZeroPowerBehavior;
    private DcMotor.RunMode runMode = DcMotorConfig.defaultRunMode;

    public MotorBuilder(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    @Deprecated
    public MotorBuilder customName(String customName) {
        this.customName = customName;
        return this;
    }

    public MotorBuilder zeroPowerState(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        this.zeroPowerBehavior = zeroPowerBehavior;
        return this;
    }

    public MotorBuilder direction(DcMotorSimple.Direction direction) {
        this.direction = direction;
        return this;
    }

    public MotorBuilder runMode(DcMotor.RunMode runMode) {
        this.runMode = runMode;
        return this;
    }

    private void setMotorProperties(DcMotor motor) {
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        motor.setMode(runMode);
    }

    private DcMotor getMotor() throws RuntimeException {
        try {
            return HardwareManager.getHardwareDevice(DcMotor.class, hardwareName);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error getting motor: " + hardwareName, e);
        }
    }

    public DcMotorEx build() {
        DcMotor motor = getMotor();
        setMotorProperties(motor);

        assert motor instanceof DcMotorEx;
        return (DcMotorEx) motor;
    }
}
