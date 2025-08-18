package RoboCore.MachineLearning.Weka_PIDF;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PIDFLogger {
    private final List<PIDFData> dataList = new ArrayList<>();

    public void log(PIDFData data) {
        dataList.add(data);
    }

    public void saveToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.println("mode,target,actual,error,p,i,d,f,output,deltaTime,motorCurrent");
            for (PIDFData d : dataList) {
                writer.println(d.toCSV());
            }
        }
    }

    public List<PIDFData> getDataList() {
        return dataList;
    }
}
