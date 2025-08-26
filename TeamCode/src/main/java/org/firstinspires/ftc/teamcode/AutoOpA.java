package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.RoboCoreLinearOpMode;

import com.panther_tech.RoboCore.Drivetrains.MecanumDrivetrain;
import com.panther_tech.RoboCore.Robot;
import com.panther_tech.RoboCore.RoboCore;

/** @noinspection unused*/
@Autonomous
public class AutoOpA extends RoboCoreLinearOpMode implements MecanumDrivetrain {

    @SuppressWarnings("all")
    private Robot robot;

    @Override
    public void init() {
        robot = new Robot.Builder(this)
                .build();
    }

    @Override
    public void runOpMode() {
        move(0, 0, 0, RoboCore.MeasurementUnit.MM);
    }
}
