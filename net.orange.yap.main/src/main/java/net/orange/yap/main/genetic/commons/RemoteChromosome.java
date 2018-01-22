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

    private final Machine machine;
    private Double fitness;

    RemoteChromosome(Program program, EvaluationStrategy evaluation) {
        super(program, evaluation);
        this.machine = getEvaluation().getRuntime().createMachine(getProgram());
    }

    public Machine getMachine() {
        return machine;
    }

    public boolean isEvaluated() {
        return fitness != null;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public double fitness() {
        return isEvaluated() ? fitness : -1f;
    }

    public void execute() {
        getEvaluation().getRuntime().execute(machine);
    }

    public String toCodeString() {
        return getEvaluation().getRuntime().asString(getProgram());
    }
}
