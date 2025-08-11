package RoboCore.Tools;

import RoboCore.RoboCore;

/**
 * The type Boolean tools.
 */
public abstract class BooleanTools extends RoboCore {
    /**
     * Gets if object exists. Returns a true or false if the object is not null or null.
     *
     * @param x Object
     * @return True if object is not null, therefore it exists.
     */
    public static boolean exists(Object x) {
        return x != null;
    }
}
