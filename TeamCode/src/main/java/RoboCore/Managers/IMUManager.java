package RoboCore.Managers;

import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import RoboCore.RoboCore;

public class IMUManager extends RoboCore {

    private static IMUManager instance;
    private final IMU imu;
    public static float X;
    public static float Y;
    public static float Z;

    private IMUManager(IMU.Parameters parameters) {
        new CommandArchitecture.Builder(this.getClass()).name("IMUManager").type(RoboCore.CommandType.UPDATE).attachMethod("update").build();

        imu = HardwareManager.findHardwareDeviceByClass(IMU.class).get(0);
        imu.initialize(parameters);
        imu.resetYaw();
    }

    public final void update() {
        Orientation robotOrientation = imu.getRobotOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.RADIANS);

        X = robotOrientation.firstAngle;
        Y = robotOrientation.secondAngle;
        Z = robotOrientation.thirdAngle;

    }

    public IMU getIMU() {
        return imu;
    }

    public static IMUManager getInstance(IMU.Parameters parameters) {
        if (instance == null) {
            instance = new IMUManager(parameters);
        }
        return instance;
    }
}
