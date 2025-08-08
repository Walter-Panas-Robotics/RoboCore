package RoboCore.Managers;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import RoboCore.RoboCore;

public class CommandArchitechture extends RoboCore {

    public static List<Command> update_commands = new ArrayList<>();
    private static volatile CommandArchitechture instance;
    private static String name;

    private CommandArchitechture() {

    }

    public synchronized static CommandArchitechture getInstance() {
        if (instance == null) {
            instance = new CommandArchitechture();
        }
        return instance;
    }

    public static class Command {

        public Builder builder;
        public CommandType type;
        public Method method;
        public String name;

        public Command(Builder builder) {
            this.builder = builder;
            this.name = builder.name;
            this.type = builder.type;
            this.method = builder.attachedMethod;

        }
    }

    @SuppressWarnings("NotNullFieldNotInitialized")
    public static class Builder {

        @NonNull
        private final Class<?> attachedClass;

        public String name;

        @NonNull
        private CommandType type;

        @NonNull
        private Method attachedMethod;

        public Builder(@NonNull Class<?> attachedClass) {
            this.attachedClass = attachedClass;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(@NonNull CommandType type) {
            switch (type) {
                case UPDATE:
                    this.type = CommandType.UPDATE;
                    break;
            }
            return this;
        }

        public Builder attachMethod(String methodName) {
            try {
                attachedMethod = attachedClass.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Error adding method: " + methodName, e);
            }

            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
