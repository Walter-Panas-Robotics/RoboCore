package RoboCore.Managers;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

import RoboCore.RoboCore;

public class HardwareManager extends RoboCore {
    private static HardwareManager instance;
    private static HardwareMap hardwareMap;

    public static <T> T getHardwareDevice(Class<? extends T> deviceClass, String deviceName) throws RuntimeException {
        if (!exists(hardwareMap)) {
            throw new RuntimeException("Hardware map is null");
        }
        T device;
        try {
            device = hardwareMap.get(deviceClass, deviceName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting hardware device: " + deviceName, e);
        }
        return device;
    }

    public static <T> List<T> findHardwareDeviceByClass(Class<? extends T> deviceClass) {
        if (!exists(hardwareMap)) {
            throw new RuntimeException("Hardware map is null");
        }
        return hardwareMap.getAll(deviceClass);
    }

    @SuppressWarnings("unused")
    public static HardwareMap getInstance(HardwareMap hardwareMap) {
        if (instance == null) {
            instance = new HardwareManager();
            HardwareManager.hardwareMap = hardwareMap;
        }
        return hardwareMap;
    }
}
