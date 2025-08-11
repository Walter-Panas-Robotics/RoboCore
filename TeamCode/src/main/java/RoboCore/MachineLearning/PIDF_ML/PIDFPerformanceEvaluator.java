package RoboCore.MachineLearning.PIDF_ML;

public class PIDFPerformanceEvaluator {
    public static float evaluate(MotorTelemetry telemetry) {
        float errorRatio = Math.abs(telemetry.error) / Math.max(1f, telemetry.target);
        float velocityStability = 1f / (1f + Math.abs(telemetry.velocity - telemetry.target));
        float currentPenalty = telemetry.current / 1000f;

        return (1f - errorRatio) * velocityStability - currentPenalty;
    }
}
