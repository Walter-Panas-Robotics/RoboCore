package RoboCore.MachineLearning.PIDF_ML;

import java.io.Serializable;

public class PIDFModel implements Serializable {
    private final float[][] w1 = new float[6][8];
    private final float[] b1 = new float[8];
    private final float[][] w2 = new float[8][4];
    private final float[] b2 = new float[4];

    public float[] predict(float[] input) {
        float[] h1 = relu(matMul(input, w1, b1));
        return matMul(h1, w2, b2);
    }

    public void updateWeights(float[] input, float[] targetPIDF, float learningRate) {
        float[] h1 = relu(matMul(input, w1, b1));
        float[] output = matMul(h1, w2, b2);

        float[] error = new float[4];
        for (int i = 0; i < 4; i++) error[i] = targetPIDF[i] - output[i];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                w2[j][i] += learningRate * error[i] * h1[j];
            }
            b2[i] += learningRate * error[i];
        }
    }

    private float[] matMul(float[] input, float[][] weights, float[] bias) {
        float[] output = new float[weights[0].length];
        for (int i = 0; i < output.length; i++) {
            output[i] = bias[i];
            for (int j = 0; j < input.length; j++) {
                output[i] += input[j] * weights[j][i];
            }
        }
        return output;
    }

    private float[] relu(float[] x) {
        float[] out = new float[x.length];
        for (int i = 0; i < x.length; i++) out[i] = Math.max(0, x[i]);
        return out;
    }
}
