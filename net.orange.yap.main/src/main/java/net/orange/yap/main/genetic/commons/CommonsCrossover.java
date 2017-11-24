package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.eval.EvaluationStrategy;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ChromosomePair;
import org.apache.commons.math3.genetics.CrossoverPolicy;

import java.util.Objects;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 15:15
 */
public class CommonsCrossover implements CrossoverPolicy {

    private final YapRuntime runtime;
    private final EvaluationStrategy evaluation;

    public CommonsCrossover(EvaluationStrategy evaluation) {
        Objects.requireNonNull(evaluation, "An evaluation strategy is required.");
        this.runtime = evaluation.getRuntime();
        this.evaluation = evaluation;
    }

    @Override
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        CommonsChromosome one = (CommonsChromosome) first;
        CommonsChromosome two = (CommonsChromosome) second;
        CommonsChromosome c1 = new CommonsChromosome(runtime.generator().combine(one.getProgram(), two.getProgram()), evaluation);
        CommonsChromosome c2 = new CommonsChromosome(runtime.generator().combine(two.getProgram(), one.getProgram()), evaluation);

        return new ChromosomePair(c1, c2);
    }
}
