package RoboCore.MachineLearning.PIDF_ML;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class ScenarioHandler {
    private final boolean isVelocityMode;

    public ScenarioHandler(boolean isVelocityMode) {
        this.isVelocityMode = isVelocityMode;
    }

    public MotorTelemetry collectTelemetry(DcMotorEx motor, float target) {
        return new MotorTelemetry(motor, target, isVelocityMode);
    }
}
