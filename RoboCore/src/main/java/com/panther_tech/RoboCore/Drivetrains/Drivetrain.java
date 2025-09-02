package com.panther_tech.RoboCore.Drivetrains;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.HashMap;
import java.util.Map;

import com.panther_tech.RoboCore.RoboCore;
import com.panther_tech.RoboCore.Robot;

@SuppressWarnings("unused")
public interface Drivetrain extends DrivetrainImpl {

    /**
     * Drive the robot in TeleOp portions. Always feeds in a list of motors.
     * Every interface must implement this or every OpMode needs to implement this.
     * Failure to implement this will result in a crash.
     *
     * @param motors The motors to drive the robot with. Fed in by RoboCore package.
     */
    void drive(Robot robot, Map<RoboCore.MotorLocation, DcMotorEx> motors);

}
