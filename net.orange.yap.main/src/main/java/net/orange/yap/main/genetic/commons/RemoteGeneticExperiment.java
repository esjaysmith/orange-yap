package net.orange.yap.main.genetic.commons;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User: sjsmit
 * Date: 20/01/2018
 * Time: 20:53
 */
public class RemoteGeneticExperiment {

    private final CommonsGeneticAlgorithmBuilder builder;
    private GeneticAlgorithm algorithm;
    private Population population;

    public RemoteGeneticExperiment(CommonsGeneticAlgorithmBuilder builder) {
        Objects.requireNonNull(builder, "A builder is required in order to obtain genetic algorithms.");
        this.builder = builder;
    }

    public void evolveUntilCompletion(StoppingCondition condition) {
        newExperiment();
        algorithm.evolve(population, condition);
    }

    @SuppressWarnings("WeakerAccess")
    public void newExperiment() {
        Pair<GeneticAlgorithm, Population> pair = builder.build();
        this.algorithm = pair.getFirst();
        this.population = pair.getSecond();
    }

    public List<RemoteChromosome> listNewChromosomes() {
        if (population == null) {
            newExperiment();
        }
        List<RemoteChromosome> chromosomes = new ArrayList<>();
        for (Chromosome next : population) {
            RemoteChromosome chromosome = (RemoteChromosome) next;
            if (!chromosome.isEvaluated()) {
                chromosomes.add(chromosome);
            }
        }
        return chromosomes;
    }

    public void nextGeneration() {
        if (algorithm == null) {
            newExperiment();
        } else {
            // Will throw an exception on unevaluated chromosomes when using a RemoteEvaluationStrategy.
            this.population = algorithm.nextGeneration(population);
        }
    }
}
