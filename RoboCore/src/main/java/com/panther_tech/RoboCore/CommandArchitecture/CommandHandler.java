package com.panther_tech.RoboCore.CommandArchitecture;

import com.panther_tech.RoboCore.RoboCore;
import com.panther_tech.RoboCore.Robot;
import com.panther_tech.RoboCore.RoboCore.CommandPriority;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;

@Getter
public class CommandHandler {

    private static CommandHandler instance;

    private static final Telemetry telemetry = Robot.getInstance().getTelemetry();

    private static final ArrayList<Command> commands = new ArrayList<>();

    public static void poll() {
        commands.sort((a, b) -> b.priority.compareTo(a.priority));
        for (Command command : commands) {
            command.execute();
        }
    }

    public static void register(CommandPriority priority, Commandable command) {
        if (priority == null) {
            priority = CommandPriority.NORMAL;
        }

        commands.add(new Command(command, priority));
    }

    private static class Command {
        private final Commandable command;
        private final CommandPriority priority;

        public Command(Commandable command, CommandPriority priority) {
            this.command = command;
            this.priority = priority;
        }

        public void execute() {
            command.update();
        }

    }
}
