package RoboCore.MachineLearning.PIDF_ML;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class MotorTelemetry {
    public float target;
    public float actual;
    public float error;
    public float velocity;
    public float current;
    public boolean isVelocityMode;

    public MotorTelemetry(DcMotorEx motor, float target, boolean isVelocityMode) {
        this.target = target;
        this.isVelocityMode = isVelocityMode;
        this.actual = isVelocityMode ? (float) motor.getVelocity() : motor.getCurrentPosition();
        this.error = target - actual;
        this.velocity = (float) motor.getVelocity();
        this.current = (float) motor.getCurrent(CurrentUnit.MILLIAMPS);
    }

    public float[] toInputArray() {
        return new float[]{target, actual, error, velocity, current, isVelocityMode ? 1f : 0f};
    }
}