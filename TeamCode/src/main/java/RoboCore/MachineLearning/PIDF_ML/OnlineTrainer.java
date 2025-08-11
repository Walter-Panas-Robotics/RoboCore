package RoboCore.MachineLearning.PIDF_ML;

public class OnlineTrainer {
    private PIDFModel model;
    private float baseLearningRate = 0.001f;

    public OnlineTrainer(PIDFModel model) {
        this.model = model;
    }

    public void train(MotorTelemetry telemetry, float[] truePIDF) {
        float score = PIDFPerformanceEvaluator.evaluate(telemetry);
        float adaptiveRate = baseLearningRate * score;
        model.updateWeights(telemetry.toInputArray(), truePIDF, adaptiveRate);
    }
}
