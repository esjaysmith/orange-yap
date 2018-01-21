package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.machine.stack.Program;
import org.apache.commons.math3.genetics.Chromosome;

/**
 * User: sjsmit
 * Date: 21/01/2018
 * Time: 20:52
 */
public class RemoteChromosomeFactoryImpl implements ChromosomeFactory {

    private final EvaluationStrategy evaluation;

    public RemoteChromosomeFactoryImpl(EvaluationStrategy evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public YapRuntime getRuntime() {
        return evaluation.getRuntime();
    }

    @Override
    public Chromosome createChromosome() {
        YapRuntime runtime = evaluation.getRuntime();
        return createChromosome(runtime.generator().generate());
    }

    @Override
    public Chromosome createChromosome(Program program) {
        return new RemoteChromosome(program, evaluation);
    }
}
