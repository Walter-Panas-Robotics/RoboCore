package RoboCore.Drivetrains;

import static RoboCore.Managers.GamepadManager.gamepad1;
import static RoboCore.RoboCore.DirectionalVector;
import static RoboCore.RoboCore.MotorLocation;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.Map;
import java.util.Objects;

import RoboCore.Managers.IMUManager;
import RoboCore.Robot;

public interface MecanumDrivetrain extends Drivetrain {

    default void drive(Robot robot, Map<MotorLocation, DcMotorEx> motors) {

        DcMotorEx front_left_motor = Objects.requireNonNull(
                motors.get(MotorLocation.FRONT_LEFT),
                "Front Left motor (FRONT_LEFT) is missing from the motors map."
        );
        DcMotorEx back_left_motor = Objects.requireNonNull(
                motors.get(MotorLocation.BACK_LEFT),
                "Back Left motor (BACK_LEFT) is missing from the motors map."
        );
        DcMotorEx front_right_motor = Objects.requireNonNull(
                motors.get(MotorLocation.FRONT_RIGHT),
                "Front Right motor (FRONT_RIGHT) is missing from the motors map."
        );
        DcMotorEx back_right_motor = Objects.requireNonNull(
                motors.get(MotorLocation.BACK_RIGHT),
                "Back Right motor (BACK_RIGHT) is missing from the motors map."
        );

        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;

        double robot_angle = IMUManager.X;

        double combined_magnitude = Math.min(1, Math.sqrt(x * x + y * y + rx * rx));
        double final_magnitude = combined_magnitude < robot.getControllerDeadzone() ? 0 : combined_magnitude;

        double targetSpeed = robot.getMaxSpeed() * final_magnitude;

        double leftFrontSpeed, rightFrontSpeed, leftBackSpeed, rightBackSpeed;

        double maxSpeed = robot.getMaxSpeed();

        if (!robot.isFieldCentric()) {

            leftFrontSpeed = Math.min(maxSpeed, (y + x + rx) * targetSpeed);
            rightFrontSpeed = Math.min(maxSpeed, (y - x + rx) * targetSpeed);
            leftBackSpeed = Math.min(maxSpeed, (y - x - rx) * targetSpeed);
            rightBackSpeed = Math.min(maxSpeed, (y + x - rx) * targetSpeed);

        } else {

            double rotatedX = x * Math.cos(-robot_angle) - y * Math.sin(-robot_angle);
            double rotatedY = x * Math.sin(-robot_angle) + y * Math.cos(-robot_angle);

            leftFrontSpeed = Math.min(maxSpeed, (rotatedY + rotatedX + rx) * targetSpeed);
            rightFrontSpeed = Math.min(maxSpeed, (rotatedY - rotatedX + rx) * targetSpeed);
            leftBackSpeed = Math.min(maxSpeed, (rotatedY - rotatedX - rx) * targetSpeed);
            rightBackSpeed = Math.min(maxSpeed, (rotatedY + rotatedX - rx) * targetSpeed);

        }

        front_left_motor.setVelocity(leftFrontSpeed);
        back_left_motor.setVelocity(leftBackSpeed);
        front_right_motor.setVelocity(rightFrontSpeed);
        back_right_motor.setVelocity(rightBackSpeed);

    }

    default void move(DirectionalVector direction, double distance, double speed) {
    }

    default void turn(double angle, double speed) {
    }
}
