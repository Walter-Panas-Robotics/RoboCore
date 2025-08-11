package RoboCore.Managers;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import RoboCore.RoboCore;
import RoboCore.Robot;

public class GamepadManager extends RoboCore {

    private static final Map<Boolean, Method> gamepad_commands = new HashMap<>();
    private static final Map<Method, Boolean> lastTick_gamepad_list = new HashMap<>();
    private static GamepadManager instance;

    private GamepadManager() {
        new CommandArchitechture.Builder(this.getClass())
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

        for (Map.Entry<Boolean, Method> entry : gamepad_commands.entrySet()) {
            if (entry.getKey() && Boolean.FALSE.equals(lastTick_gamepad_list.get(entry.getValue()))) {
                try {
                    entry.getValue().invoke(null);
                } catch (Exception e) {
                    throw new RuntimeException("Error updating command: " + entry.getValue().getName(), e);
                }
            }
        }

    }

    public static void assignCommand(boolean gamepad_button, String actionableMethod) {

        Method actionable_method = Robot.getMethodFromName(Robot.class, actionableMethod);

        gamepad_commands.put(gamepad_button, actionable_method);
        lastTick_gamepad_list.put(actionable_method, gamepad_button);

    }
}
