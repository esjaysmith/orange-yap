package net.orange.yap.machine.eval;

import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 21/11/2017
 * Time: 17:18
 */
public interface EvaluationStrategy {

    YapRuntime getRuntime();

    double fitness(Program p);
}
