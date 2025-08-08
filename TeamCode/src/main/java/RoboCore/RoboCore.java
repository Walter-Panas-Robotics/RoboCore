package RoboCore;

import androidx.annotation.NonNull;

public class RoboCore {

    static double convertToMM(double value, @NonNull MeasurementUnit unit) {
        switch (unit) {
            case IN:
                return value * 25.4;
            case MM:
                return value;
            case CM:
                return value * 10;
            default:
                throw new IllegalArgumentException("Unsupported unit: " + unit);
        }
    }

    public enum MotorLocation {FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT}

    public enum CommandType {UPDATE}

    public enum MeasurementUnit {IN, MM, CM}
}
