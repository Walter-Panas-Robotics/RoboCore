package com.panther_tech.RoboCore.Drivetrains;

import com.panther_tech.RoboCore.RoboCore;

public interface AutonomousDrivetrain extends DrivetrainImpl {

    /**
     * Move the robot in a specific direction with a given distance, speed, and angle;
     * */
    void move(double distance, double speed, double angle, RoboCore.MeasurementUnit unit);


    /**
     * Turn the robot in a specific direction with a given angle and speed;
     */
    void turn(double angle, double speed);

}

