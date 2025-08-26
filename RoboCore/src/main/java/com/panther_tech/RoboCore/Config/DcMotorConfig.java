package com.panther_tech.RoboCore.Config;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class DcMotorConfig {
    public static final DcMotorSimple.Direction defaultDirection = DcMotorSimple.Direction.FORWARD;
    public static final DcMotor.ZeroPowerBehavior defaultZeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
    public static final DcMotor.RunMode defaultRunMode = DcMotor.RunMode.RUN_USING_ENCODER;

}
