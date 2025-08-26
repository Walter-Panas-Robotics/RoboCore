package com.panther_tech.RoboCore.Managers;


import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.panther_tech.RoboCore.RoboCore;
import com.panther_tech.RoboCore.Robot;


@SuppressWarnings("unused")
public class GamepadManager extends RoboCore {

    private static final List<GamepadCommandEntry> gamepad_commands = new ArrayList<>();
    public static Gamepad gamepad1;
    public static Gamepad gamepad2;
    private static GamepadManager instance;

    private GamepadManager(OpMode opMode) {
        new CommandArchitecture.Builder(this.getClass())
                .name("GamepadManager:Updater")
                .type(CommandType.UPDATE)
                .attachMethod("update")
                .build();

        gamepad1 = opMode.gamepad1;
        gamepad2 = opMode.gamepad2;
    }

    public static GamepadManager getInstance(@Nullable OpMode opMode) {
        if (instance == null) {
            assert opMode != null;
            instance = new GamepadManager(opMode);
        }
        return instance;
    }

    public static void update() {

        for (GamepadCommandEntry entry : gamepad_commands) {
            if (entry.gamepad_button) {
                try {
                    switch (entry.triggerType) {
                        case ON_PRESS:
                            if (!entry.lastTriggerValue) {
                                entry.actionableMethod.invoke(null);
                            }
                            break;
                        case ON_RELEASE:
                            if (entry.lastTriggerValue) {
                                entry.actionableMethod.invoke(null);
                            }
                            break;
                        case ON_HOLD:
                            entry.actionableMethod.invoke(null);
                            break;
                        case SWITCH:
                            if (!entry.lastTriggerValue) {
                                entry.holdState = !entry.holdState;
                            }
                            break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error updating command: " + entry.actionableMethod.getName(), e);
                }
            }
            try {
                if (entry.holdState) {
                    entry.actionableMethod.invoke(null);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error updating command: " + entry.actionableMethod.getName(), e);
            }
            entry.lastTriggerValue = entry.gamepad_button;
        }

    }

    public static void assignCommand(boolean gamepad_button, String actionableMethod, @Nullable TriggerType triggerType) {

        Method actionable_method;

        try {
            actionable_method = getMethodFromName(Robot.class, actionableMethod);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Actionable method not found: " + actionableMethod, e);
        }

        gamepad_commands.add(new GamepadCommandEntry(gamepad_button, actionable_method, triggerType));
    }

    public static void assignCommand(boolean gamepad_button, String actionableMethod) {
        assignCommand(gamepad_button, actionableMethod, null);
    }
}
