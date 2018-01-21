package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.Machine;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 15:15
 */
public class RemoteChromosome extends CommonsChromosome {
    private Double fitness;

    RemoteChromosome(Program program, EvaluationStrategy evaluation) {
        super(program, evaluation);
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double fitness() {
        if (!isEvaluated()) {
            this.fitness = super.fitness();
        }
        return fitness;
    }

    public boolean isEvaluated() {
        return fitness != null;
    }

    public Machine execute() {
        final Machine m = getEvaluation().getRuntime().createMachine(getProgram());

        return m;
    }

    public String toCodeString() {
        return getEvaluation().getRuntime().asString(getProgram());
    }
}
