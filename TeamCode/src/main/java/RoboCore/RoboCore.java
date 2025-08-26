package RoboCore;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.reflect.Method;

/**
 * The type Robo core.
 */
@SuppressWarnings("unused")
public class RoboCore {
    public static double version;
    public static String author;
    public static String team;

    public static DcMotor.RunMode autonomousRunMode = DcMotor.RunMode.RUN_TO_POSITION;
    public static DcMotor.RunMode teleopRunMode = DcMotor.RunMode.RUN_USING_ENCODER;

    public static boolean AUTO_CONFIG_MOTORS = true;


    public static Method getMethodFromName(Class<?> classInstance, String actionableMethod) throws NoSuchMethodException {
        return classInstance.getMethod(actionableMethod);
    }

    /**
     * Convert to mm double.
     *
     * @param value the value
     * @param unit  the unit
     * @return Millimeter value
     */
    public static double convertToMM(double value, @NonNull MeasurementUnit unit) {
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    // Intentional inversion as I want it to be true if the object exists (not null).
    public static boolean exists(Object x) {
        return x != null;
    }

    /**
     * The enum Holonomic motor locations.
     */
    public enum MotorLocation {FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT, LEFT, RIGHT, FL_SWERVE, FR_SWERVE, BL_SWERVE, BR_SWERVE}

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
        ACTION
    }

    /**
     * The enum Measurement unit.
     */
    public enum MeasurementUnit {
        /**
         * Inches measurement unit.
         */
        IN {
            @Override
            public double convertToMM(double value) {
                return value * 25.4;
            }

            @Override
            public double convertToMotorTicks(double value) {

                double wheelDiameter = Robot.getInstance().getWheelDiameter();
                double ticksPerRevolution = Robot.getInstance().getTicksPerRevolution();

                return wheelDiameter != 0 && ticksPerRevolution != 0 ? convertToMM(value) / (wheelDiameter * Math.PI) * ticksPerRevolution : 0;
            }
        },

        /**
         * Millimeter measurement unit.
         */
        MM {
            @Override
            public double convertToMM(double value) {
                return value;
            }

            @Override
            public double convertToMotorTicks(double value) {

                double wheelDiameter = Robot.getInstance().getWheelDiameter();
                double ticksPerRevolution = Robot.getInstance().getTicksPerRevolution();

                return wheelDiameter != 0 && ticksPerRevolution != 0 ? convertToMM(value) / (wheelDiameter * Math.PI) * ticksPerRevolution : 0;
            }
        },

        /**
         * Centimeter measurement unit.
         */
        CM {
            @Override
            public double convertToMM(double value) {
                return value * 10;
            }

            @Override
            public double convertToMotorTicks(double value) {

                double wheelDiameter = Robot.getInstance().getWheelDiameter();
                double ticksPerRevolution = Robot.getInstance().getTicksPerRevolution();

                return wheelDiameter != 0 && ticksPerRevolution != 0 ? convertToMM(value) / (wheelDiameter * Math.PI) * ticksPerRevolution : 0;
            }
        };

        protected abstract double convertToMM(double value);

        public abstract double convertToMotorTicks(double value);
    }

    public enum CommandPriority {
        HIGHEST,
        HIGH,
        MEDIUM,
        LOW,
        EXPENDABLE
    }

    public enum TriggerType {
        ON_PRESS,
        ON_RELEASE,
        ON_HOLD,
        SWITCH
    }

    public enum MathConstants {
        ;

        double ZERO() {
            return 0;
        }

        double TWOSQRT2() {
            return 2 * Math.sqrt(2);
        }

        double SQRT2() {
            return Math.sqrt(2);
        }

        double PI() {
            return 3.141592653589793;
        }

        double E() {
            return 2.718281828459045;
        }
    }

    public enum DirectionalVector {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN,
    }
}
