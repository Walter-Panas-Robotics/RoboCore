package RoboCore.Managers; // Or your preferred package

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class TelemetryManager {

    private final Telemetry telemetry;
    private final LinkedHashMap<String, Object> dataPoints;
    private String logFileName = "telemetry_log.csv";
    private Writer fileWriter;
    private boolean loggingEnabled = false;
    private boolean firstLogWrite = true;
    private final ElapsedTime logTimer = new ElapsedTime();
    private double logIntervalSeconds = 0.1; // Log data 10 times per second

    public static final String TAG = "TelemetryManager";
    public static final String LOG_DIR = "TelemetryLogs"; // Folder on Robot Controller

    public TelemetryManager(Telemetry telemetry, boolean enableLogging) {
        this.telemetry = telemetry;
        this.dataPoints = new LinkedHashMap<>();
        this.loggingEnabled = enableLogging;

        if (this.loggingEnabled) {
            setupLogging();
        }
        // Set telemetry to monospace for better alignment (if available and desired)
        // This is a bit of a hack and might not work on all Driver Station versions
        // or might make the font less readable for some.
        // telemetry.log().setDisplayOrder(Telemetry.Log.DisplayOrder.OLDEST_FIRST);
        // telemetry.log().setCapacity(15); // Adjust as needed
        // telemetry.setItemSeparator(""); // Remove default item separator for manual formatting
        // telemetry.setCaptionValueSeparator(""); // Remove default caption:value separator
    }

    public TelemetryManager(Telemetry telemetry) {
        this(telemetry, false); // Logging disabled by default
    }

    private void setupLogging() {
        try {
            File logDir = new File(System.getProperty("user.home") + "/FIRST/" + LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            // Add timestamp to filename to avoid overwriting
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            logFileName = "telemetry_log_" + timestamp + ".csv";
            File logFile = new File(logDir, logFileName);

            fileWriter = new FileWriter(logFile);
            RobotLog.i(TAG, "Logging telemetry to: " + logFile.getAbsolutePath());
            firstLogWrite = true;
            logTimer.reset();
        } catch (IOException e) {
            RobotLog.e(TAG, "Error setting up telemetry log file: " + e.getMessage());
            loggingEnabled = false; // Disable logging if setup fails
        }
    }

    public void addData(String caption, Object value) {
        dataPoints.put(caption, value);
    }

    public void addLine(String line) {
        // For adding separator lines or custom text lines
        dataPoints.put("LINE_" + dataPoints.size(), line);
    }

    public void addSpace() {
        addData("", ""); // Adds an empty line essentially
    }

    public void update() {
        telemetry.clearAll(); // Clear previous telemetry

        StringBuilder logLine = new StringBuilder();
        if (loggingEnabled && firstLogWrite) {
            // Write CSV Header
            for (String caption : dataPoints.keySet()) {
                if (!caption.startsWith("LINE_")) { // Don't log lines/separators as columns
                    logLine.append(caption.replace(",", ";")).append(","); // Sanitize caption for CSV
                }
            }
            if (logLine.length() > 0) logLine.deleteCharAt(logLine.length() - 1); // Remove last comma
            logLine.append("\n");
        }

        StringBuilder currentDataLine = new StringBuilder();

        for (Map.Entry<String, Object> entry : dataPoints.entrySet()) {
            String caption = entry.getKey();
            Object value = entry.getValue();

            if (caption.startsWith("LINE_")) {
                telemetry.addLine(String.valueOf(value));
            } else if (caption.isEmpty() && String.valueOf(value).isEmpty()) {
                telemetry.addLine(""); // Empty line for spacing
            }
            else {
                // Format for display (adjust field widths as needed)
                String formattedValue;
                if (value instanceof Double || value instanceof Float) {
                    formattedValue = String.format(Locale.US, "%.3f", value);
                } else {
                    formattedValue = String.valueOf(value);
                }
                telemetry.addData(String.format(Locale.US, "%-20s", caption), formattedValue);

                // Prepare data for logging
                if (loggingEnabled && !caption.startsWith("LINE_")) {
                    currentDataLine.append(String.valueOf(value).replace(",", ";")).append(",");
                }
            }
        }

        if (loggingEnabled && fileWriter != null && logTimer.seconds() >= logIntervalSeconds) {
            if (currentDataLine.length() > 0) {
                currentDataLine.deleteCharAt(currentDataLine.length() - 1); // Remove last comma
                logLine.append(currentDataLine.toString()).append("\n");
            }

            try {
                fileWriter.write(logLine.toString());
                if(firstLogWrite) firstLogWrite = false; // Header written
            } catch (IOException e) {
                RobotLog.e(TAG, "Error writing to telemetry log: " + e.getMessage());
            }
            logTimer.reset();
        }

        telemetry.update();
        // dataPoints.clear(); // Clear data for the next loop iteration if you want to redefine all data each time
        // Or keep it if you want to update existing values
    }

    public void setLoggingEnabled(boolean enable) {
        if (enable && !this.loggingEnabled) { // If turning on and was off
            this.loggingEnabled = true;
            setupLogging();
        } else if (!enable && this.loggingEnabled) { // If turning off and was on
            closeLog();
            this.loggingEnabled = false;
        }
    }

    public void setLogInterval(double seconds) {
        this.logIntervalSeconds = Math.max(0.01, seconds); // Ensure a minimum interval
    }


    public void closeLog() {
        if (loggingEnabled && fileWriter != null) {
            try {
                fileWriter.flush();
                fileWriter.close();
                RobotLog.i(TAG, "Telemetry log closed: " + logFileName);
            } catch (IOException e) {
                RobotLog.e(TAG, "Error closing telemetry log: " + e.getMessage());
            }
            fileWriter = null;
        }
    }
}