package RoboCore.MachineLearning.PIDF_ML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MotorLogExporter {
    private final List<String> logs = new ArrayList<>();

    public void log(MotorTelemetry telemetry, float[] pidf) {
        logs.add(String.format(Locale.US,
                "%.2f,%.2f,%.2f,%.2f,%.2f,%d,%.4f,%.4f,%.4f,%.4f",
                telemetry.target, telemetry.actual, telemetry.error,
                telemetry.velocity, telemetry.current,
                telemetry.isVelocityMode ? 1 : 0,
                pidf[0], pidf[1], pidf[2], pidf[3]
        ));
    }

    public void save(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("target,actual,error,velocity,current,mode,P,I,D,F\n");
            for (String line : logs) writer.write(line + "\n");
        }
    }
}
