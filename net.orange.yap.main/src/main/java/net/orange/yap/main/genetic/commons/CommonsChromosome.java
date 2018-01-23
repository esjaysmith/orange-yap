package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.machine.stack.Program;
import org.apache.commons.math3.genetics.Chromosome;

import java.util.Objects;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 15:15
 */
public class CommonsChromosome extends Chromosome {
    private final Program program;
    private final EvaluationStrategy evaluation;

    CommonsChromosome(Program program, EvaluationStrategy evaluation) {
        this.program = program;
        this.evaluation = evaluation;
    }

    public Program getProgram() {
        return program;
    }

    YapRuntime getRuntime() {
        return evaluation.getRuntime();
    }

    @Override
    public double fitness() {
        return evaluation.fitness(program);
    }

    @Override
    protected boolean isSame(Chromosome another) {
        if (another == this) {
            return true;
        }
        if (!(another instanceof CommonsChromosome)) {
            return false;
        }

        CommonsChromosome other = (CommonsChromosome) another;
        return Objects.equals(program, other.program);
    }

    @Override
    public String toString() {
        return program + " with fitness=" + fitness();
    }
}
