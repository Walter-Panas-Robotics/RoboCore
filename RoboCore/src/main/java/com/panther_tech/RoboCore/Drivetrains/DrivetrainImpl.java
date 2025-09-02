package com.panther_tech.RoboCore.Drivetrains;

import com.panther_tech.RoboCore.RoboCore;
import com.panther_tech.RoboCore.Robot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.HashMap;
import java.util.Map;

interface DrivetrainImpl {
    Map<RoboCore.MotorLocation, DcMotorEx> motors = new HashMap<>();

    /**
     * Initialize the drivetrain. Sets the motor map within the drivetrain.
     * If in Autonomous mode, sets the motor mode to RUN_TO_POSITION by default. Otherwise, sets it to RUN_USING_ENCODER.
     * Can be overridden if needed.
     *
     * @param robot The robot to initialize the drivetrain with. (Uses "this" keyword)
     * @see RoboCore
     */
    default void init(Robot robot) {
        Drivetrain.motors.putAll(robot.getInternalMotors());

        if (RoboCore.AUTO_CONFIG_MOTORS) {
            if (robot.isAutonomous()) {
                motors.forEach((location, motor) -> motor.setMode(RoboCore.autonomousRunMode));
            } else {
                motors.forEach((location, motor) -> motor.setMode(RoboCore.teleopRunMode));
            }
        }
    }

    default void stop(OpMode opmode) {
        opmode.stop();
    }
}
