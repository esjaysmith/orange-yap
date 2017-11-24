package net.orange.yap.main.evaluation;

import net.orange.yap.machine.Machine;
import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.util.MachineUtils;
import net.orange.yap.util.evaluation.Accuracy;
import net.orange.yap.util.lang.StringUtils;

/**
 * User: sjsmit
 * Date: 10/08/15
 * Time: 23:19
 */
public class BooleanParityProblem implements EvaluationStrategy {

    private final YapRuntime runtime;
    private final int parity;
    private int evaluations;

    public BooleanParityProblem(YapRuntime runtime, int parity) {
        this.runtime = runtime;
        this.parity = parity;
    }

    @Override
    public double fitness(Program p) {
        return evaluate(p);
    }

    @Override
    public YapRuntime getRuntime() {
        return runtime;
    }

    private float evaluate(Program program) {
        final int programLength = MachineUtils.countPoints(program);
        if (programLength == 0) {
            return 0f;
        }

        final Accuracy fitness = new Accuracy();
        final int max = (int) Math.pow(2, parity);
        boolean processNext = true;
        for (int v = 0; v < max && processNext; v++) {
            // Encode the integer as bits. The string has correct length.
            final char[] binary = StringUtils.toBinary(v, parity);
            // Prepare the test array and compute the required result.
            int amount = 0, idx = 0;
            boolean[] input = new boolean[parity];
            for (char c : binary) {
                final boolean b = c == '1';
                input[idx++] = b;
                amount += b ? 1 : 0;
            }
            final boolean expected = amount % 2 == 0;
            final boolean actual = evaluate(program, input);
            fitness.addResult(expected ? 1f : 0f, actual ? 1f : 0f);
            processNext = expected == actual;
        }

        evaluations++;
        float accuracy = fitness.getAccuracy();
        if (processNext) {
            // Subtract a penalty for program length once the problem is solved.
            // The penalty must be small enough not to interfere with the fitness value based on accuracy.
            float penalty = 1e-5f;
            accuracy -= penalty * programLength;
        }
        return accuracy;
    }

    @Override
    public String toString() {
        return getClass().getName() + " evaluations = " + evaluations + ".";
    }

    private boolean evaluate(Program program, boolean... booleans) {
        final Machine m = runtime.createMachine(program);
        for (int i = booleans.length - 1; i >= 0; i--) {
            m.booleans().push(booleans[i]);
        }
        runtime.execute(m);
        return m.booleans().size() < 1 ? false : m.booleans().peek();
    }
}
