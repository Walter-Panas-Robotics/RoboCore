package RoboCore.Managers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

import RoboCore.RoboCore;
import RoboCore.Robot;

public class HardwareManager extends RoboCore {
    static HardwareMap hardwareMap = Robot.hardwareMap;

    public static <T> T getHardwareDevice(Class<? extends T> deviceClass, String deviceName) throws RuntimeException {
        T device;
        try {
            device = hardwareMap.get(deviceClass, deviceName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting hardware device: " + deviceName, e);
        }
        return device;
    }

    public static <T> List<T> findHardwareDeviceByClass(Class<? extends T> deviceClass) {
        return hardwareMap.getAll(deviceClass);
    }
}
