package net.orange.yap.main.genetic.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sjsmit
 * Date: 22/11/2017
 * Time: 11:12
 */
public class EvolutionHistory {

    private final List<CommonsChromosome> bestChromosomes;
    private CommonsChromosome overallBest;

    public EvolutionHistory() {
        this.bestChromosomes = new ArrayList<>();
    }

    public CommonsChromosome getBestChromosome() {
        return overallBest;
    }

    public List<CommonsChromosome> getSuccessiveWinners() {
        return bestChromosomes;
    }

    public void addGenerationFittest(CommonsChromosome chromosome) {
        if (overallBest == null || chromosome.fitness() > overallBest.fitness()) {
            bestChromosomes.add(chromosome);
            overallBest = chromosome;
        }
    }
}
