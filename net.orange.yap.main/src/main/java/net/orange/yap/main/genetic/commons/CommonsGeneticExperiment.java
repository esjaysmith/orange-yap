package net.orange.yap.main.genetic.commons;

import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.apache.commons.math3.util.Pair;

import java.util.Objects;

/**
 * User: sjsmit
 * Date: 20/01/2018
 * Time: 20:53
 */
public class CommonsGeneticExperiment {

    private final CommonsGeneticAlgorithmBuilder builder;
    private GeneticAlgorithm experiment;
    private Population population;

    public CommonsGeneticExperiment(CommonsGeneticAlgorithmBuilder builder) {
        Objects.requireNonNull(builder, "A builder is required in order to obtain genetic algorithms.");
        this.builder = builder;
    }

    public EvolutionHistory evolveUntilCompletion(EvolutionHistory history, StoppingCondition condition) {
        Pair<GeneticAlgorithm, Population> pair = builder.build();
        pair.getFirst().evolve(pair.getSecond(), condition);
        return history;
    }

    public void create() {
        Pair<GeneticAlgorithm, Population> pair = builder.build();
        this.experiment = pair.getFirst();
        this.population = pair.getSecond();
    }
}
