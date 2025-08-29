package com.panther_tech.RoboCore.CommandArchitecture;

import com.panther_tech.RoboCore.RoboCore;

public abstract class Commandable extends RoboCore {

    public Commandable() {
        CommandHandler.register(priority, this);
    }

    public CommandPriority priority = null;

    protected abstract void update();

}
