package net.orange.yap.main.genetic.commons;

import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.TournamentSelection;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.util.Pair;

/**
 * User: sjsmit
 * Date: 21/11/2017
 * Time: 20:12
 */
public class CommonsGeneticAlgorithmBuilder {

    private final ChromosomeFactory chromosomeFactory;
    private int population_limit = 200;
    private int tournament_arity = 5;
    private float elitism_rate = .5f;
    private float crossover_rate = .5f;
    private float mutation_rate = .5f;

    public CommonsGeneticAlgorithmBuilder(ChromosomeFactory factory) {
        GeneticAlgorithm.setRandomGenerator(new JDKRandomGenerator(0));
        this.chromosomeFactory = factory;
    }

    public int getPopulationLimit() {
        return population_limit;
    }

    public CommonsGeneticAlgorithmBuilder setPopulationLimit(int limit) {
        this.population_limit = limit;
        return this;
    }

    public int getTournamentArity() {
        return tournament_arity;
    }

    public CommonsGeneticAlgorithmBuilder setTournamentArity(int arity) {
        this.tournament_arity = arity;
        return this;
    }

    public float getElitismRate() {
        return elitism_rate;
    }

    public CommonsGeneticAlgorithmBuilder setElitismRate(float rate) {
        this.elitism_rate = rate;
        return this;
    }

    public float getCrossoverRate() {
        return crossover_rate;
    }

    public CommonsGeneticAlgorithmBuilder setCrossoverRate(float rate) {
        this.crossover_rate = rate;
        return this;
    }

    public float getMutationRate() {
        return mutation_rate;
    }

    public CommonsGeneticAlgorithmBuilder setMutationRate(float rate) {
        this.mutation_rate = rate;
        return this;
    }

    Pair<GeneticAlgorithm, Population> build() {
        // Initialize a new genetic algorithm.
        final CommonsCrossover crossover = new CommonsCrossover(chromosomeFactory);
        final CommonsMutation mutation = new CommonsMutation(chromosomeFactory);
        final TournamentSelection selection = new TournamentSelection(tournament_arity);
        final GeneticAlgorithm ga = new GeneticAlgorithm(crossover, crossover_rate, mutation, mutation_rate, selection);

        // Create the initial population.
        final Population initial = new ElitisticListPopulation(population_limit, elitism_rate);
        while (initial.getPopulationSize() < population_limit) {
            initial.addChromosome(chromosomeFactory.createChromosome());
        }

        return new Pair<>(ga, initial);
    }
}
