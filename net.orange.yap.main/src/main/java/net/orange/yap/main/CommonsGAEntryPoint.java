package net.orange.yap.main;

import net.orange.yap.machine.YapRuntimeFactory;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.main.evaluation.RemoteEvaluationStrategy;
import net.orange.yap.main.genetic.commons.ChromosomeFactory;
import net.orange.yap.main.genetic.commons.CommonsGeneticAlgorithmBuilder;
import net.orange.yap.main.genetic.commons.RemoteChromosome;
import net.orange.yap.main.genetic.commons.RemoteChromosomeFactoryImpl;
import org.apache.commons.cli.*;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.Pair;
import py4j.GatewayServer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sjsmit
 * Date: 20/01/2018
 * Time: 20:40
 */
public class CommonsGAEntryPoint {

    private final CommonsGeneticAlgorithmBuilder builder;
    private GeneticAlgorithm algorithm;
    private Population population;

    private CommonsGAEntryPoint(CommonsGeneticAlgorithmBuilder builder) {
        this.builder = builder;
        restart();
    }

    @SuppressWarnings("WeakerAccess")
    public void restart() {
        Pair<GeneticAlgorithm, Population> pair = builder.build();
        this.algorithm = pair.getFirst();
        this.population = pair.getSecond();
    }

    public List<RemoteChromosome> listNewChromosomes() {
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
        // Will throw an exception on unevaluated chromosomes.
        this.population = algorithm.nextGeneration(population);
    }

    public RemoteChromosome getFittestChromosome() {
        return (RemoteChromosome) population.getFittestChromosome();
    }

    public static void main(String[] args) {
        Options options = new Options();
        MainUtil.addBasicOptions(options);

        try {
            CommandLine command = new DefaultParser().parse(options, args);
            if (command.hasOption("help")) {
                new HelpFormatter().printHelp("CommonsGeneticSearch", options);
            } else {
                final YapRuntimeFactory runtimeFactory = MainUtil.createYapRuntimeFactory(command);
                final EvaluationStrategy evaluation = new RemoteEvaluationStrategy(runtimeFactory.create());
                final ChromosomeFactory chromosomeFactory = new RemoteChromosomeFactoryImpl(evaluation);
                final CommonsGeneticAlgorithmBuilder builder = MainUtil.createBuilder(command, chromosomeFactory);
                final GatewayServer gatewayServer = new GatewayServer(new CommonsGAEntryPoint(builder));
                String overview = "Gateway server for push machine evolution settings:\n" +
                        "dry-run:\t\t" + command.hasOption("dry-run") + "\n" +
                        "max-points:\t\t" + runtimeFactory.getMaximumProgramPoints() + "\n" +
                        "max-instructions:\t" + runtimeFactory.getMaximumExecutionInstructions() + "\n" +
                        "max-stack-size:\t\t" + runtimeFactory.getMaximumStackDepth() + "\n" +
                        "population-size:\t" + builder.getPopulationLimit() + "\n" +
                        "tournament-arity:\t" + builder.getTournamentArity() + "\n" +
                        "elitism-rate:\t\t" + builder.getElitismRate() + "\n" +
                        "crossover-rate:\t\t" + builder.getCrossoverRate() + "\n" +
                        "mutation-rate:\t\t" + builder.getMutationRate();
                System.out.println(overview);
                if (!command.hasOption("dry-run")) {
                    gatewayServer.start();
                    System.out.println("Server started on port " + gatewayServer.getPort() + ".");
                }
            }
        } catch (ParseException e) {
            System.err.println("Command line parsing failed. Reason: " + e.getMessage());
        }
    }
}
