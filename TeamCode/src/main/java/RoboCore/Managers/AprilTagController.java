package RoboCore.Managers;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

import RoboCore.Robot;

@SuppressWarnings("unused")
public class AprilTagController {

    private static final AprilTagProcessor aprilTagProcessor = new AprilTagProcessor.Builder()
            .setDrawAxes(true)
            .setDrawTagOutline(true)
            .setDrawCubeProjection(true)
            .build();
    private static final VisionPortal visionPortal = new VisionPortal.Builder()
            .addProcessor(aprilTagProcessor)
            .enableLiveView(true)
            .setCamera(HardwareManager.findHardwareDeviceByClass(WebcamName.class).get(0))
            .build();
    private static AprilTagController instance;


    private AprilTagController() {

    }


    public static AprilTagController getInstance(Robot robot) {
        if (instance == null) {
            instance = new AprilTagController();
        }
        return instance;
    }

    public List<AprilTagDetection> update() {
        return aprilTagProcessor.getDetections();
    }
}
