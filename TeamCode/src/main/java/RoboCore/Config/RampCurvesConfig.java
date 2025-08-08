package RoboCore.Config;

public class RampCurvesConfig {
    /**
     * BasicCurve - If true, the normalize function will always return the closest extreme (min_val and max_val) if the input is outside of the range, instead of erroring.
     *
     * @see RoboCore.RampCurves.BasicCurve
     **/
    public static boolean returnExtremes = false;
}
