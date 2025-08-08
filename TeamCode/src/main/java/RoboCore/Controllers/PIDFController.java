package RoboCore.Controllers;

import static RoboCore.Tools.BooleanTools.exists;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.Map;

import RoboCore.Exceptions.MotorNotFound;
import RoboCore.RoboCore;
import RoboCore.Robot;

public class PIDFController extends RoboCore {

    private static final Map<String, DcMotorEx> motors = Robot.getMotors();

    public static void setCoefficients(String motorName, double p, double i, double d, double f) {
        if (!exists(motors.get(motorName))) {
            throw new MotorNotFound("Motor not found: " + motorName);
        }
    }

}
