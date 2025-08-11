package RoboCore.MachineLearning.PIDF_ML;

import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

public class PIDFWeightSaver {
    public static void saveWeights(PIDFModel model, File file) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
                out.writeObject(model);
            }
        }
    }

    public static PIDFModel loadWeights(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (PIDFModel) in.readObject();
        }
    }
}
