package com.panther_tech.RoboCore.Managers;

import java.lang.reflect.Method;
import java.util.UUID;

import com.panther_tech.RoboCore.RoboCore;

public class GamepadCommandEntry {
    public final UUID id;
    public final RoboCore.TriggerType triggerType;
    public boolean gamepad_button;
    public Method actionableMethod;
    public boolean lastTriggerValue = false;
    public boolean holdState = false;


    public GamepadCommandEntry(boolean gamepad_button, Method actionableMethod, RoboCore.TriggerType triggerType) {
        this.id = UUID.randomUUID();
        this.gamepad_button = gamepad_button;
        this.actionableMethod = actionableMethod;
        if (!RoboCore.exists(triggerType)) {
            triggerType = RoboCore.TriggerType.ON_PRESS;
        }
        this.triggerType = triggerType;


    }
}
