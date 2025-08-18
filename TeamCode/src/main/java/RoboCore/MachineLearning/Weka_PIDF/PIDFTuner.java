package RoboCore.MachineLearning.Weka_PIDF;

public class PIDFTuner {
    private final PIDFPredictor predictor;

    public PIDFTuner(PIDFPredictor predictor) {
        this.predictor = predictor;
    }

    public void applyTuning(PIDFData input) throws Exception {
        String modeSuffix = input.mode.toString().toLowerCase();

        input.p = predictor.predict("models/p_" + modeSuffix + ".model", input, "p");
        input.i = predictor.predict("models/i_" + modeSuffix + ".model", input, "i");
        input.d = predictor.predict("models/d_" + modeSuffix + ".model", input, "d");
        input.f = predictor.predict("models/f_" + modeSuffix + ".model", input, "f");

        // Apply to motor controller here
        System.out.printf("Tuned PIDF: P=%.4f I=%.4f D=%.4f F=%.4f%n", input.p, input.i, input.d, input.f);
    }
}
