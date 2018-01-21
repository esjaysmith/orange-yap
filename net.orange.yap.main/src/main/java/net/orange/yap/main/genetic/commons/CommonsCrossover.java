package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.YapRuntime;
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

    private final ChromosomeFactory factory;
    private final YapRuntime runtime;

    CommonsCrossover(ChromosomeFactory factory) {
        Objects.requireNonNull(factory, "An chromosome factory is required.");
        this.factory = factory;
        this.runtime = factory.getRuntime();
    }

    @Override
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        CommonsChromosome one = (CommonsChromosome) first;
        CommonsChromosome two = (CommonsChromosome) second;
        Chromosome c1 = factory.createChromosome(runtime.generator().combine(one.getProgram(), two.getProgram()));
        Chromosome c2 = factory.createChromosome(runtime.generator().combine(two.getProgram(), one.getProgram()));
        return new ChromosomePair(c1, c2);
    }
}
