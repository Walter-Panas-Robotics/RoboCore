package com.panther_tech.RoboCore;

import com.panther_tech.RoboCore.Exceptions.DoubleUpdateCall;

public class RealtimeAnalyzer {
    static boolean didTick = false;

    public static void setTick() {
        if (!didTick) {
            didTick = true;
        } else {
            throw new DoubleUpdateCall("[RoboCore.Robot]<CRITICAL>: update() or tick() cannot be called twice in a row. If using RoboCoreOpMode, remove a call from the loop() method.");
        }
    }

    public static void clearTick() {
        didTick = false;
    }
}
