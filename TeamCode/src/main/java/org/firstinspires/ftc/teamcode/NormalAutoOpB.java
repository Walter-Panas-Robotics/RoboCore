package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class NormalAutoOpB extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //Do all your initialization here.

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            // only use the loop if you're using teleop.
        }

        //Do your autonomous stuff here. if not doing teleop

    }
}
