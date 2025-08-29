package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.RoboCoreOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.panther_tech.RoboCore.Builders.Motors.MotorBuilder;
import com.panther_tech.RoboCore.Drivetrains.MecanumDrivetrain;
import com.panther_tech.RoboCore.RoboCore;
import com.panther_tech.RoboCore.Robot;

@TeleOp(name = "TeleOpB", group = "TeleOp")
@SuppressWarnings("unused")
public class TeleOpB extends RoboCoreOpMode implements MecanumDrivetrain {

    public void init() {
        robot = new Robot.Builder(this)
                .addMotor(  // Sets a custom name for the motor if you wanted to call it from another class.
                        RoboCore.MotorLocation.FRONT_LEFT, // Set motor location.
                        new MotorBuilder("fl_motor")
                                .zeroPowerState(DcMotor.ZeroPowerBehavior.FLOAT)
                                .runMode(DcMotor.RunMode.RUN_USING_ENCODER)
                                .build())
                .addMotor(
                        RoboCore.MotorLocation.BACK_LEFT,
                        new MotorBuilder("bl_motor")
                                .zeroPowerState(DcMotor.ZeroPowerBehavior.FLOAT)
                                .runMode(DcMotor.RunMode.RUN_USING_ENCODER)
                                .build())
                .addMotor(
                        RoboCore.MotorLocation.FRONT_RIGHT,
                        new MotorBuilder("fr_motor")
                                .zeroPowerState(DcMotor.ZeroPowerBehavior.FLOAT)
                                .runMode(DcMotor.RunMode.RUN_USING_ENCODER)
                                .build())
                .addMotor(
                        RoboCore.MotorLocation.BACK_RIGHT,
                        new MotorBuilder("br_motor")
                                .zeroPowerState(DcMotor.ZeroPowerBehavior.FLOAT)
                                .runMode(DcMotor.RunMode.RUN_USING_ENCODER)
                                .build())
                .addGamepadCommand(gamepad1.a, this::a_function, RoboCore.TriggerType.ON_PRESS)
                .addGamepadCommand(gamepad1.b, "b_function", RoboCore.TriggerType.ON_HOLD)
                .setWheelProperties(4, 537.7, RoboCore.MeasurementUnit.IN)
                .autoConfigure()
                .build();

        telemetry.addData("Status", "Initialized");
    }

    public void loop() {}

    public void a_function() {

    }

    public void b_function() {

    }
}
