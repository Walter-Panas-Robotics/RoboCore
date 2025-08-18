package RoboCore.Drivetrains;

import static RoboCore.Managers.GamepadManager.gamepad1;
import static RoboCore.RoboCore.MotorLocation;
import static RoboCore.Robot.controllerDeadzone;
import static RoboCore.Robot.maxSpeed;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.Map;

import RoboCore.Managers.IMUManager;
import RoboCore.Robot;

public interface MecanumDrivetrain {

    default void drive(Map<MotorLocation, DcMotorEx> motors) {
        DcMotorEx front_left_motor = motors.get(MotorLocation.FRONT_LEFT);
        DcMotorEx back_left_motor = motors.get(MotorLocation.BACK_LEFT);
        DcMotorEx front_right_motor = motors.get(MotorLocation.FRONT_RIGHT);
        DcMotorEx back_right_motor = motors.get(MotorLocation.BACK_RIGHT);

        assert back_left_motor != null;
        assert front_left_motor != null;
        assert front_right_motor != null;
        assert back_right_motor != null;
        drive(front_left_motor, back_left_motor, front_right_motor, back_right_motor);
    }

    default void drive(Map<MotorLocation, DcMotorEx> motors, IMU imu) {
        drive(motors);
    }

    default void drive(DcMotorEx front_left_motor, DcMotorEx back_left_motor, DcMotorEx front_right_motor, DcMotorEx back_right_motor) {
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double rx = gamepad1.right_stick_x;

        double angle = Math.atan2(y, x);
        double robot_angle = IMUManager.X;

        double combined_magnitude = Math.min(1, Math.sqrt(x * x + y * y + rx * rx));
        double final_magnitude = combined_magnitude < controllerDeadzone ? 0 : combined_magnitude;

        double targetSpeed = Robot.maxSpeed * final_magnitude;

        double leftFrontSpeed = 0, rightFrontSpeed = 0, leftBackSpeed = 0, rightBackSpeed = 0;

        if (!Robot.useFieldCentric) {
            leftFrontSpeed = Math.min(maxSpeed, (y + x + rx) * targetSpeed);
            rightFrontSpeed = Math.min(maxSpeed, (y - x + rx) * targetSpeed);
            leftBackSpeed = Math.min(maxSpeed, (y - x - rx) * targetSpeed);
            rightBackSpeed = Math.min(maxSpeed, (y + x - rx) * targetSpeed);
        } else {
            double rotated_x = Math.cos(angle - robot_angle);
            double rotated_y = Math.sin(angle - robot_angle);

            leftFrontSpeed = Math.min(maxSpeed, (rotated_y + rotated_x + rx) * targetSpeed);
            rightFrontSpeed = Math.min(maxSpeed, (rotated_y - rotated_x + rx) * targetSpeed);
            leftBackSpeed = Math.min(maxSpeed, (rotated_y - rotated_x - rx) * targetSpeed);
            rightBackSpeed = Math.min(maxSpeed, (rotated_y + rotated_x - rx) * targetSpeed);
        }

        front_left_motor.setVelocity(leftFrontSpeed);
        back_left_motor.setVelocity(leftBackSpeed);
        front_right_motor.setVelocity(rightFrontSpeed);
        back_right_motor.setVelocity(rightBackSpeed);

    }
}
