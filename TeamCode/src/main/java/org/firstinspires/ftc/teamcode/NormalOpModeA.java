package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class NormalOpModeA extends OpMode {

    DcMotorEx fl_motor;
    DcMotorEx fr_motor;
    DcMotorEx bl_motor;
    DcMotorEx br_motor;

    @Override
    public void init() {
        fl_motor = hardwareMap.get(DcMotorEx.class, "fl_motor");
        fr_motor = hardwareMap.get(DcMotorEx.class, "fr_motor");
        bl_motor = hardwareMap.get(DcMotorEx.class, "bl_motor");
        br_motor = hardwareMap.get(DcMotorEx.class, "br_motor");

        fl_motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        fr_motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        bl_motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        br_motor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        fl_motor.setDirection(DcMotorSimple.Direction.REVERSE);
        bl_motor.setDirection(DcMotorSimple.Direction.REVERSE);

        fl_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br_motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void loop() {

        //gamepad1 and gamepad2 are derived from OpMode

        gamepad1.left_stick_y = -gamepad1.left_stick_y; // Its reversed so we need to flip it; Up = positive. Down = negative.

        fl_motor.setVelocity(gamepad1.left_stick_y);
        fr_motor.setVelocity(gamepad1.left_stick_y);
        bl_motor.setVelocity(gamepad1.left_stick_y);
        br_motor.setVelocity(gamepad1.left_stick_y);

    }
}
