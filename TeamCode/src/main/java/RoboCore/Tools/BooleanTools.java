package RoboCore.Tools;

import RoboCore.RoboCore;

public abstract class BooleanTools extends RoboCore {
    public static boolean exists(Object x) {
        return x != null;
    }
}
