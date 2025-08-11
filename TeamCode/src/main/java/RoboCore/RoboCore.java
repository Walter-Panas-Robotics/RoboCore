package RoboCore;

import androidx.annotation.NonNull;

/**
 * The type Robo core.
 */
public class RoboCore {

    /**
     * Convert to mm double.
     *
     * @param value the value
     * @param unit  the unit
     * @return Millimeter value
     */
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

    /**
     * The enum Holonomic motor locations.
     */
    public enum MotorLocation { FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT, LEFT, RIGHT, FL_SWERVE, FR_SWERVE, BL_SWERVE, BR_SWERVE }

    /**
     * The enum Command type.
     */
    public enum CommandType {
        /**
         * Update command type.
         */
        UPDATE,
        /**
         * Action command type.
         */
        ACTION}

    /**
     * The enum Measurement unit.
     */
    public enum MeasurementUnit {
        /**
         * Inches measurement unit.
         */
        IN,
        /**
         * Millimeter measurement unit.
         */
        MM,
        /**
         * Centimeter measurement unit.
         */
        CM}
}
