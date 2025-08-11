package RoboCore.Drivetrains;

import static RoboCore.RoboCore.MotorLocation;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.Map;

public interface MecanumDrivetrain {

    void drive(DcMotorEx front_left_motor, DcMotorEx back_left_motor, DcMotorEx front_right_motor, DcMotorEx back_right_motor);

    default void drive(Map<MotorLocation, DcMotorEx> motors) {
        DcMotorEx front_left_motor = motors.get(MotorLocation.FRONT_LEFT);
        DcMotorEx back_left_motor = motors.get(MotorLocation.BACK_LEFT);
        DcMotorEx front_right_motor = motors.get(MotorLocation.FRONT_RIGHT);
        DcMotorEx back_right_motor = motors.get(MotorLocation.BACK_RIGHT);

        drive(front_left_motor, back_left_motor, front_right_motor, back_right_motor);
    }

    default void drive(Map<MotorLocation, DcMotorEx> motors, IMU imu) {
        drive(motors);
    }
}
