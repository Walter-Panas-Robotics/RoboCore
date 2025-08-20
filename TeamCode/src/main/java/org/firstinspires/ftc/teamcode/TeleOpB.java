package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import RoboCore.Builders.Motors.MotorBuilder;
import RoboCore.Drivetrains.MecanumDrivetrain;
import RoboCore.RoboCore;
import RoboCore.Robot;

@TeleOp(name = "TeleOpB", group = "TeleOp")
@SuppressWarnings("unused")
public class TeleOpB extends OpMode implements MecanumDrivetrain {

    Robot robot;

    @Override
    public void init() {
        robot = new Robot.Builder(this)
                .addMotor(  // Sets a custom name for the motor if you wanted to call it from another class.
                        RoboCore.MotorLocation.FRONT_LEFT, // Set motor location.
                        new MotorBuilder("motorA") // Create a new motor builder. This returns a DcMotorEx.
                                .direction(DcMotor.Direction.REVERSE)
                                .zeroPowerState(DcMotor.ZeroPowerBehavior.FLOAT)
                                .runMode(DcMotor.RunMode.RUN_USING_ENCODER)
                                .build())
                .addGamepadCommand(gamepad1.a, "a_function", RoboCore.TriggerType.SWITCH)
                .build();

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        robot.tick();
        robot.getMotor("motor1").setPower(gamepad1.left_stick_y);
        telemetry.addData("Status", "Running");
    }

    public void a_function() {

    }
}
