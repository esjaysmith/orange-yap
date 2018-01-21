package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.YapRuntime;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.MutationPolicy;

import java.util.Objects;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 15:15
 */
public class CommonsMutation implements MutationPolicy {

    private final ChromosomeFactory factory;
    private final YapRuntime runtime;

    CommonsMutation(ChromosomeFactory factory) {
        Objects.requireNonNull(factory, "An chromosome factory is required.");
        this.factory = factory;
        this.runtime = factory.getRuntime();
    }

    @Override
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        CommonsChromosome chromo = (CommonsChromosome) original;
        return factory.createChromosome(runtime.generator().mutate(chromo.getProgram()));
    }
}
