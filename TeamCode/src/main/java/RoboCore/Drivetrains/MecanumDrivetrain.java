package RoboCore.Drivetrains;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public interface MecanumDrivetrain {

    void drive(DcMotorEx front_left_motor, DcMotorEx back_left_motor, DcMotorEx front_right_motor, DcMotorEx back_right_motor);

}
