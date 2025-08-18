package RoboCore.MachineLearning.Weka_PIDF;

public class PIDFData {
    public ControlMode mode;
    public double target;
    public double actual;
    public double error;
    public double p, i, d, f;
    public double output;
    public double deltaTime;
    public double motorCurrent;

    public String toCSV() {
        return mode + "," + target + "," + actual + "," + error + "," +
                p + "," + i + "," + d + "," + f + "," +
                output + "," + deltaTime + "," + motorCurrent;
    }
}
