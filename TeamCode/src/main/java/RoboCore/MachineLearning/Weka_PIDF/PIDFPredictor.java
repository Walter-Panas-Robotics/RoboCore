package RoboCore.MachineLearning.Weka_PIDF;

import java.io.File;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

public class PIDFPredictor {
    private final Instances structure;

    public PIDFPredictor(String csvTemplatePath) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(csvTemplatePath));
        structure = loader.getDataSet();
    }

    public double predict(String modelPath, PIDFData input, String targetAttribute) throws Exception {
        Classifier model = (Classifier) SerializationHelper.read(modelPath);
        structure.setClass(structure.attribute(targetAttribute));

        Instance instance = new DenseInstance(structure.numAttributes());
        instance.setDataset(structure);

        instance.setValue(structure.attribute("mode"), input.mode.toString());
        instance.setValue(structure.attribute("target"), input.target);
        instance.setValue(structure.attribute("actual"), input.actual);
        instance.setValue(structure.attribute("error"), input.error);
        instance.setValue(structure.attribute("output"), input.output);
        instance.setValue(structure.attribute("deltaTime"), input.deltaTime);
        instance.setValue(structure.attribute("motorCurrent"), input.motorCurrent);

        return model.classifyInstance(instance);
    }
}
