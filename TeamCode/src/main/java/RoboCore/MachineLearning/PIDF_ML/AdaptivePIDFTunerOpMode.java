package RoboCore.MachineLearning.PIDF_ML;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@TeleOp(name = "Adaptive PIDF Tuner")
public class AdaptivePIDFTunerOpMode extends LinearOpMode {
    private PIDFModel model;
    private PIDFReinforcementAgent agent;
    private ScenarioHandler scenario;
    private MotorLogExporter logger;
    private Map<String, DcMotorEx> motors = new HashMap<>();

    @Override
    public void runOpMode() {
        String[] motorNames = {"frontLeft", "frontRight", "backLeft", "backRight"};
        for (String name : motorNames) {
            motors.put(name, hardwareMap.get(DcMotorEx.class, name));
        }

        File weightFile = new File(Environment.getExternalStorageDirectory(), "pidf_weights.dat");
        try {
            model = PIDFWeightSaver.loadWeights(weightFile);
        } catch (Exception e) {
            model = new PIDFModel();
        }

        agent = new PIDFReinforcementAgent(model);
        scenario = new ScenarioHandler(true);
        logger = new MotorLogExporter();

        waitForStart();

        while (opModeIsActive()) {
            float vx = gamepad1.left_stick_y;
            float vy = gamepad1.left_stick_x;
            float omega = gamepad1.right_stick_x;

            Map<String, Float> motorTargets = VectorIntent.resolveMecanumIntent(vx, vy, omega);

            for (Map.Entry<String, Float> entry : motorTargets.entrySet()) {
                DcMotorEx motor = motors.get(entry.getKey());
                float targetVelocity = entry.getValue() * 1500f;

                MotorTelemetry telemetry = scenario.collectTelemetry(motor, targetVelocity);
                PIDFManager manager = new PIDFManager(motor, model);
                manager.applyPIDF(telemetry);
                agent.updatePolicy(telemetry, manager.getCurrentPIDF());
                logger.log(telemetry, manager.getCurrentPIDF());

                this.telemetry.addData(entry.getKey(), "Target: %.2f, Actual: %.2f", telemetry.target, telemetry.actual);
            }

            telemetry.update();
        }

        try {
            PIDFWeightSaver.saveWeights(model, weightFile);
            File logFile = new File(Environment.getExternalStorageDirectory(), "pidf_log.csv");
            logger.save(logFile);
        } catch (IOException e) {
            telemetry.addData("Save Error", e.getMessage());
            telemetry.update();
        }
    }
}