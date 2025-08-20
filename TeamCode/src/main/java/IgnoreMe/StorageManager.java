package IgnoreMe;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import RoboCore.Config.StorageManagerConfig;
import RoboCore.RoboCore;
import RoboCore.Robot;

@SuppressWarnings({"unused", "deprecation", "CommentedOutCode"})
@Deprecated
public class StorageManager extends RoboCore {

    private static final Telemetry telemetry = Robot.getInstance().getTelemetry();

    /**
     * Checks if the controller has been initialized.
     *
     * @see Robot
     */
    private static void checkInitialized() throws RuntimeException {
//        if (!robot.initialized) {
//            System.out.println("[NATO | PersistentStorageController - Error]: Not initialized.");
//            throw new RuntimeException("[NATO | PersistentStorageController - Error]: Not initialized.");
//        }
        throw new RuntimeException("StorageManager: Cannot be used at this time.");
    }

    /**
     * Adjusts the path of the file to be created.
     *
     * @param fileName the name of the file to adjust
     * @return the adjusted path of the file
     * @see StorageManager
     */
    private static String adjustPath(String fileName) {
        checkInitialized();
        if (!fileName.startsWith(StorageManagerConfig.preferredPath)) {
            fileName = StorageManagerConfig.preferredPath + fileName;
        }
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        return fileName;
    }

    /**
     * Creates a new file with the given name.
     *
     * @param fileName the name of the file to create
     **/
    public static boolean createFile(String fileName) {
        checkInitialized();
        boolean result = false;
        String adjustedFileName = adjustPath(fileName);
        try {
            File writeFile = new File(adjustedFileName);
            result = writeFile.createNewFile();
        } catch (IOException e) {
            telemetry.addLine("[PersistentStorageController - IOException]: " + e);
        }
        return result;
    }

    /**
     * Creates a new file with the given name and content.
     *
     * @param fileName the name of the file to create
     * @param content  the content to write to the file
     **/
    public boolean createFile(String fileName, String content) {
        checkInitialized();
        boolean result = false;
        String adjustedFileName = adjustPath(fileName);
        try {
            File writeFile = new File(adjustedFileName);
            if (writeFile.createNewFile()) {
                result = writeToFile(adjustedFileName, content);
            }
        } catch (IOException e) {
            telemetry.addLine("[PersistentStorageController - IOException]: " + e);
        }
        return result;
    }

    /**
     * Write content to a file.
     *
     * @param fileName the name of the file to write to
     * @return Successful or not. Boolean.
     **/
    public boolean writeToFile(String fileName, String content) {
        checkInitialized();
        boolean result = false;
        String adjustedFileName = adjustPath(fileName);
        try {
            File writeFile = new File(adjustedFileName);
            FileWriter writer = new FileWriter(adjustedFileName);
            writer.write(content);
            writer.close();
            result = true;
        } catch (IOException e) {
            telemetry.addLine("[PersistentStorageController - IOException]: " + e);
        }
        return result;
    }

    /**
     * Delete a file.
     *
     * @param fileName  the name of the file to delete
     * @param deleteKey the key to delete the file. defined in NATO.PersistentStorage
     * @return Successful or not. Boolean.
     * @see StorageManagerConfig
     **/
    public boolean deleteFile(String fileName, String deleteKey) throws Exception {
        checkInitialized();
        boolean result = false;
        String adjustedFileName = adjustPath(fileName);
        File writeFile = new File(adjustedFileName);
        if (deleteKey.equals(StorageManagerConfig.deletePIN)) {
            if (writeFile.delete()) {
                result = true;
            }
        } else {
            throw new Exception("[PersistentStorageController]: Invalid delete key. File: \"" + fileName + "\" was not deleted.");
        }
        return result;
    }
}
