package RoboCore.MachineLearning.PIDF_ML;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class PIDFManager {
    private DcMotorEx motor;
    private PIDFModel model;

    public PIDFManager(DcMotorEx motor, PIDFModel model) {
        this.motor = motor;
        this.model = model;
    }

    public void applyPIDF(MotorTelemetry telemetry) {
        float[] pidf = model.predict(telemetry.toInputArray());
        motor.setVelocityPIDFCoefficients(pidf[0], pidf[1], pidf[2], pidf[3]);
    }

    public float[] getCurrentPIDF() {
        return model.predict(new MotorTelemetry(motor, 0, true).toInputArray());
    }
}
