package net.orange.yap.main.evaluation;

import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 20/01/2018
 * Time: 22:11
 */
public class RemoteEvaluationStrategy implements EvaluationStrategy {

    private final YapRuntime runtime;

    public RemoteEvaluationStrategy(YapRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public YapRuntime getRuntime() {
        return runtime;
    }

    @Override
    public double fitness(Program p) {
        throw new IllegalStateException("Fitness evaluation is expected not to take place in the jvm.");
    }
}
