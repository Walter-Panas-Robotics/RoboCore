package com.panther_tech.RoboCore.Managers;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagLibrary;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

import com.panther_tech.RoboCore.Robot;

@SuppressWarnings("unused")
public class AprilTagController {

    private static final AprilTagProcessor aprilTagProcessorLiveView = new AprilTagProcessor.Builder()
            .setDrawAxes(true)
            .setDrawTagOutline(true)
            .setDrawCubeProjection(true)
            .setDrawTagID(true)
            .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
            .build();
    private static final AprilTagProcessor aprilTagProcessor = new AprilTagProcessor.Builder()
            .build();
    private static final VisionPortal visionPortal = new VisionPortal.Builder()
            .addProcessors(aprilTagProcessorLiveView, aprilTagProcessor)
            .enableLiveView(Robot.getInstance().isUseLiveView())
            .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
            .setCamera(HardwareManager.findHardwareDeviceByClass(WebcamName.class).get(0))
            .build();
    private static AprilTagController instance;


    private AprilTagController() {
        visionPortal.setProcessorEnabled(aprilTagProcessor, Robot.getInstance().isUseAprilTags());
        visionPortal.setProcessorEnabled(aprilTagProcessorLiveView, Robot.getInstance().isUseAprilTags() & Robot.getInstance().isUseLiveView());
    }


    public static AprilTagController getInstance(Robot robot) {
        if (instance == null) {
            instance = new AprilTagController();
        }
        return instance;
    }

    public List<AprilTagDetection> pullTags() {
        return aprilTagProcessor.getDetections();
    }
}
