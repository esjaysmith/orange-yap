package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.YapRuntime;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.MutationPolicy;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 15:15
 */
public class CommonsMutation implements MutationPolicy {

    private final YapRuntime runtime;

    public CommonsMutation(YapRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        CommonsChromosome chromo = (CommonsChromosome) original;
        return new CommonsChromosome(runtime.generator().mutate(chromo.getProgram()), chromo.getEvaluation());
    }
}
