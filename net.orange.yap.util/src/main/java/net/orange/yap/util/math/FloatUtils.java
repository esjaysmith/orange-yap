package net.orange.yap.util.math;

/**
 * User: sjsmit
 * Date: 13/01/16
 * Time: 14:19
 */
public class FloatUtils {

    public static float sigmoid(float x) {
        float v = (float) (1d / (1d + Math.exp(-1d * x)));
        assert !Float.isNaN(v);
        return v; // sigmoid [0;1]
    }
}
