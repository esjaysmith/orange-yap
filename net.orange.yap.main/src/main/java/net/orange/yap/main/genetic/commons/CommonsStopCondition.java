package net.orange.yap.main.genetic.commons;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.FixedGenerationCount;
import org.apache.commons.math3.genetics.Population;

import java.util.logging.Logger;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 15:18
 */
public class CommonsStopCondition extends FixedGenerationCount {

    private static final Logger log = Logger.getLogger(CommonsStopCondition.class.getName());
    private final float maximumFitness;
    private Listener callback = new Listener() {
    };
    public CommonsStopCondition(int maxGenerations, float maximumFitness) throws NumberIsTooSmallException {
        super(maxGenerations);
        this.maximumFitness = maximumFitness;
    }

    public void setCallback(Listener callback) {
        this.callback = callback;
    }

    @Override
    public boolean isSatisfied(Population population) {
        final Chromosome fittestChromosome = population.getFittestChromosome();
        final float fitness = (float) fittestChromosome.getFitness();
        boolean maximumFitnessAttained = fitness > maximumFitness;
        callback.notify(getNumGenerations(), maximumFitnessAttained, population);
        return maximumFitnessAttained || super.isSatisfied(population);
    }

    public interface Listener {
        default void notify(int generation, boolean maximumFitnessAttained, Population population) {
            Chromosome c = population.getFittestChromosome();
            log.info("Generation num=" + generation + " produced the following fittest individual: " + c + ".");
        }
    }
}
