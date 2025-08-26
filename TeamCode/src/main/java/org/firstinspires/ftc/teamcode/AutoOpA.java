package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.RoboCoreLinearOpMode;

import RoboCore.Drivetrains.MecanumDrivetrain;
import RoboCore.RoboCore;
import RoboCore.Robot;


@Autonomous
public class AutoOpA extends RoboCoreLinearOpMode implements MecanumDrivetrain {

    private Robot robot;

    @Override
    public void init() {
        robot = new Robot.Builder(this)
                .build();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        move(0, 0, 0, RoboCore.MeasurementUnit.MM);
    }
}
