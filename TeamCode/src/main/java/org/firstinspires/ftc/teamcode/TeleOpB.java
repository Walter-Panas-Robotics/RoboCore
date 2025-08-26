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

    Robot robot;

    public void init_robot() {
        robot = new Robot.Builder(this)
                .addMotor(  // Sets a custom name for the motor if you wanted to call it from another class.
                        RoboCore.MotorLocation.FRONT_LEFT, // Set motor location.
                        new MotorBuilder("motorA") // Create a new motor builder. This returns a DcMotorEx.
                                .direction(DcMotor.Direction.REVERSE)
                                .zeroPowerState(DcMotor.ZeroPowerBehavior.FLOAT)
                                .runMode(DcMotor.RunMode.RUN_USING_ENCODER)
                                .build())
                .addGamepadCommand(gamepad1.a, "a_function", RoboCore.TriggerType.SWITCH)
                .setWheelProperties(4, 537.7, RoboCore.MeasurementUnit.IN)
                .build();

        telemetry.addData("Status", "Initialized");
    }

    public void loop() {}

    public void a_function() {

    }
}
