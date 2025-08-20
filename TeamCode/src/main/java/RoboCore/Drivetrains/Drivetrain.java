package RoboCore.Drivetrains;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.HashMap;
import java.util.Map;

import RoboCore.RoboCore;
import RoboCore.Robot;

@SuppressWarnings("unused")
public interface Drivetrain {
    Map<RoboCore.RoboCore.MotorLocation, DcMotorEx> motors = new HashMap<>();

    /**
     * Initialize the drivetrain. Sets the motor map within the drivetrain.
     *
     * @param robot The robot to initialize the drivetrain with. (Uses "this" keyword)
     */
    default void init(Robot robot) {
        Drivetrain.motors.putAll(robot.getInternalMotors());
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
     * @param direction The direction to move the robot
     * @param distance  The distance to move the robot
     * @param speed     The speed to move the robot
     */
    void move(RoboCore.DirectionalVector direction, double distance, double speed);

    /**
     * Turn the robot in a specific direction with a given angle and speed;
     */
    void turn(double angle, double speed);

    default void stop(OpMode opmode) {
        opmode.stop();
    }


}
