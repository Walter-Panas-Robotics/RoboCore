package com.panther_tech.RoboCore.Drivetrains;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.HashMap;
import java.util.Map;

import com.panther_tech.RoboCore.RoboCore;
import com.panther_tech.RoboCore.Robot;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
public interface Drivetrain {
    Map<RoboCore.MotorLocation, DcMotorEx> motors = new HashMap<>();

    RoboCore.DrivetrainType drivetrainType = null;

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

    /**
     * Drive the robot in TeleOp portions. Always feeds in a list of motors.
     * Every interface must implement this or every OpMode needs to implement this.
     * Failure to implement this will result in a crash.
     *
     * @param motors The motors to drive the robot with. Fed in by RoboCore package.
     */
    void drive(Robot robot, Map<RoboCore.MotorLocation, DcMotorEx> motors);

    /**
     * Move the robot in a specific direction with a given distance and speed;
     * Only supports the directional vectors of:
     * FORWARD, BACKWARD, LEFT, and RIGHT.
     *
     * @param unit      The unit to move the robot
     * @param distance  The distance to move the robot
     * @param speed     The speed to move the robot
     * @param angle     The angle to move the robot in degrees.
     */
    void move(double distance, double speed, double angle, RoboCore.MeasurementUnit unit);

    /**
     * Turn the robot in a specific direction with a given angle and speed;
     */
    void turn(double angle, double speed);

    default void stop(OpMode opmode) {
        opmode.stop();
    }


}
