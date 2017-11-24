package net.orange.yap.util.evaluation;

import static java.lang.Math.abs;
import static java.util.stream.IntStream.range;

/**
 * http://www.dataschool.io/simple-guide-to-confusion-matrix-terminology/
 * <p>
 * User: sjsmit
 * Date: 10/08/15
 * Time: 23:20
 */
public class Accuracy {
    private static final float margin = 1e-30f;

    private int n, errors;

    public void addResult(double desired, double actual) {
        addResult((float) desired, (float) actual);
    }

    public void addResult(float desired, float actual) {
        float e = abs(desired - actual);
        errors += e > margin ? 1 : 0;
        n++;
    }

    public void addResult(double[] desired, double[] actual) {
        int maxi = desired.length;
        boolean err = range(0, maxi).mapToDouble(i -> abs(desired[i] - actual[i])).anyMatch(d -> d > margin);
        errors += err ? 1 : 0;
        n++;
    }

    public float getAccuracy() {
        return 1f - getErrorRate();
    }

    public float getErrorRate() {
        return n == 0 ? 1f : (float) errors / n;
    }

    @Override
    public String toString() {
        return String.valueOf(getAccuracy());
    }
}
