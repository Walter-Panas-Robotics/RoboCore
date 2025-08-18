package RoboCore.MachineLearning.Weka_PIDF;

import java.io.File;

import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

public class PIDFModelTrainer {
    public void trainModel(String csvPath, String targetAttribute, String modelPath) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvPath));
        Instances data = loader.getDataSet();

        data.setClass(data.attribute(targetAttribute));
        LinearRegression model = new LinearRegression();
        model.buildClassifier(data);

        SerializationHelper.write(modelPath, model);
    }
}
