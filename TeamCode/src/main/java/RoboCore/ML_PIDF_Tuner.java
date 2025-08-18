package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "ML PIDF Tuner", group = "Machine Learning")
public class ML_PIDF_Tuner extends LinearOpMode {
    private static final String OUTPUT_FILE = "/sdcard/FTC_PIDF_Tuning.csv";
    // Test parameters
    private static final double VELOCITY_TARGET = 1000.0; // ticks per second
    private static final int POSITION_TARGET = 1000; // ticks
    private static final double TEST_DURATION = 2.0; // seconds
    private static final double SETTLE_TIME = 0.5; // seconds
    // PIDF search ranges
    private final double[] pRange = {0.0, 0.1, 0.2, 0.3, 0.4, 0.5};
    private final double[] iRange = {0.0, 0.01, 0.02, 0.03, 0.04};
    private final double[] dRange = {0.0, 0.01, 0.02, 0.03};
    private final double[] fRange = {0.0, 0.01, 0.02, 0.03};
    private DcMotorEx motor;
    private ElapsedTime timer;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotorEx.class, "testMotor");
        timer = new ElapsedTime();

        // Initialize motor
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        // Run tuning for both modes
        tunePIDF(Mode.VELOCITY);
        tunePIDF(Mode.POSITION);
    }

    private void tunePIDF(Mode mode) {
        List<TuningResult> results = new ArrayList<>();

        // Grid search through PIDF combinations
        for (double p : pRange) {
            for (double i : iRange) {
                for (double d : dRange) {
                    for (double f : fRange) {
                        if (!opModeIsActive()) return;

                        TuningResult result = testPIDF(p, i, d, f, mode);
                        results.add(result);

                        // Reset motor between tests
                        motor.setPower(0);
                        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        sleep((long) (SETTLE_TIME * 1000));

                        telemetry.addData("Tested", "P=%.2f, I=%.2f, D=%.2f, F=%.2f, Error=%.2f",
                                p, i, d, f, result.error);
                        telemetry.update();
                    }
                }
            }
        }

        // Save results to file
        saveResultsToFile(results, mode);

        // Find best coefficients
        TuningResult bestResult = findBestResult(results);

        telemetry.addData("Best Coefficients " + mode,
                "P=%.2f, I=%.2f, D=%.2f, F=%.2f, Error=%.2f",
                bestResult.p, bestResult.i, bestResult.d, bestResult.f, bestResult.error);
        telemetry.update();
    }

    private TuningResult testPIDF(double p, double i, double d, double f, Mode mode) {
        // Set PIDF coefficients
        motor.setVelocityPIDFCoefficients(p, i, d, f);
        if (mode == Mode.POSITION) {
            motor.setPositionPIDFCoefficients(p);
        }

        // Initialize test
        double errorSum = 0;
        int sampleCount = 0;
        timer.reset();

        // Set target based on mode
        if (mode == Mode.VELOCITY) {
            motor.setVelocity(VELOCITY_TARGET);
        } else {
            motor.setTargetPosition(POSITION_TARGET);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(0.5);
        }

        // Run test
        while (opModeIsActive() && timer.seconds() < TEST_DURATION) {
            double current = mode == Mode.VELOCITY ?
                    motor.getVelocity() : motor.getCurrentPosition();
            double target = mode == Mode.VELOCITY ?
                    VELOCITY_TARGET : POSITION_TARGET;

            errorSum += Math.abs(target - current);
            sampleCount++;

            telemetry.addData("Current", "%.2f", current);
            telemetry.update();
            sleep(10);
        }

        // Calculate average error
        double avgError = sampleCount > 0 ? errorSum / sampleCount : Double.MAX_VALUE;

        // Reset motor
        if (mode == Mode.POSITION) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        return new TuningResult(p, i, d, f, avgError);
    }

    private void saveResultsToFile(List<TuningResult> results, Mode mode) {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE + "_" + mode.toString() + ".csv")) {
            writer.write("P,I,D,F,Error\n");
            for (TuningResult result : results) {
                writer.write(String.format("%.2f,%.2f,%.2f,%.2f,%.2f\n",
                        result.p, result.i, result.d, result.f, result.error));
            }
        } catch (IOException e) {
            telemetry.addData("Error", "Failed to write results: " + e.getMessage());
            telemetry.update();
        }
    }

    private TuningResult findBestResult(List<TuningResult> results) {
        TuningResult best = results.get(0);
        for (TuningResult result : results) {
            if (result.error < best.error) {
                best = result;
            }
        }
        return best;
    }

    enum Mode {
        VELOCITY,
        POSITION
    }

    private static class TuningResult {
        double p, i, d, f, error;

        TuningResult(double p, double i, double d, double f, double error) {
            this.p = p;
            this.i = i;
            this.d = d;
            this.f = f;
            this.error = error;
        }
    }
}