package RoboCore.Managers;


import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import RoboCore.RoboCore;
import RoboCore.Robot;

public class GamepadManager extends RoboCore {

    private static final List<GamepadCommandEntry> gamepad_commands = new ArrayList<>();
    private static GamepadManager instance;

    private GamepadManager() {
        new CommandArchitecture.Builder(this.getClass())
                .name("GamepadManager:Updater")
                .type(CommandType.UPDATE)
                .attachMethod("update")
                .build();
    }

    public static GamepadManager getInstance() {
        if (instance == null) {
            instance = new GamepadManager();
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

        Method actionable_method = Robot.getMethodFromName(Robot.class, actionableMethod);

        gamepad_commands.add(new GamepadCommandEntry(gamepad_button, actionable_method, triggerType));

    }

    public static void assignCommand(boolean gamepad_button, String actionableMethod) {
        assignCommand(gamepad_button, actionableMethod, null);
    }
}
