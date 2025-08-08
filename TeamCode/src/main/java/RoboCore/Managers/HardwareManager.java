package RoboCore.Managers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DeprecatedSystem.OldRobot;

public class HardwareManager {
    static HardwareMap hardwareMap = OldRobot.hardwareMap;

    public static <T> T getHardwareDevice(Class<? extends T> deviceClass, String deviceName) throws RuntimeException {
        T device;
        try {
            device = hardwareMap.get(deviceClass, deviceName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting hardware device: " + deviceName, e);
        }
        return device;
    }
}
