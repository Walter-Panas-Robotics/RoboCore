package org.firstinspires.ftc.teamcode.DeprecatedSystem;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.reflect.Method;

import RoboCore.Managers.Storage.StorageManager;

/**
 * Main way to interact with the robot.
 *
 * @author Nick Persaud
 * @version 1.0.0 (2025)
 **/

@Deprecated
public class OldRobot<O extends OpMode> {
    public static Method driveFunction;
    public static HardwareMap hardwareMap;
    public final boolean initialized = true;
    public final Telemetry telemetry;
    public final StorageManager storageManager;
    private final OldRobotbuilder<O> builder;
    private final Class<O> opMode;
    private double lastTick;


    public OldRobot(OldRobotbuilder<O> builder) {
        this.builder = builder;
        this.telemetry = builder.getTelemetry();
        hardwareMap = builder.getHardwareMap();
        this.storageManager = new StorageManager(this);
        this.opMode = builder.getOpMode();
        try {
            driveFunction = this.opMode.getMethod("drive");
        } catch (NoSuchMethodException e) {
            telemetry.addData("Error", "Drive function not found.");
            throw new RuntimeException(e);
        }
    }

    private void updateAllSubsystems() {

    }

    /**
     * Same as the update() method but returns the time between updates/ticks in milliseconds.
     * This can be used for diagnosing any code issues.
     *
     * @return int Time between ticks in milliseconds.
     * @link update()
     **/
    public int tick() {
        updateAllSubsystems();
        int delta = (int) (System.currentTimeMillis() - lastTick);
        lastTick = System.currentTimeMillis();
        return delta;
    }


    /**
     * Update all subsystems for the robot.
     *
     * @link tick()
     **/
    public void update() {
        lastTick = System.currentTimeMillis();
        updateAllSubsystems();
    }
}
