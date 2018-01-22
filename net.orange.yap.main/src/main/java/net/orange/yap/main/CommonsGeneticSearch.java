package net.orange.yap.main;

import net.orange.yap.machine.YapRuntimeFactory;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.main.evaluation.BooleanParityProblem;
import net.orange.yap.main.genetic.commons.*;
import org.apache.commons.cli.*;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.Pair;

import java.util.logging.Logger;

import static net.orange.yap.util.lang.StringUtils.abbreviate;

/**
 * User: sjsmit
 * Date: 21/11/2017
 * Time: 16:43
 */
public class CommonsGeneticSearch {
    private static final Logger log = Logger.getLogger(CommonsGeneticSearch.class.getName());

    public static void main(String[] args) {
        Options options = new Options();
        MainUtil.addBasicOptions(options);
        options.addOption(Option.builder().longOpt("parity")
                .desc("The parity of the boolean parity problem the genetic algorithm is to solve")
                .hasArg()
                .argName("LEN")
                .build());
        options.addOption(Option.builder().longOpt("max-generations")
                .desc("The maximum number of generations the genetic algorithm can run")
                .hasArg()
                .argName("NUM")
                .build());
        options.addOption(Option.builder().longOpt("max-fitness")
                .desc("The maximum fitness to attain after which the genetic algorithm will abort")
                .hasArg()
                .argName("NUM")
                .build());

        try {
            CommandLine command = new DefaultParser().parse(options, args);
            if (command.hasOption("help")) {
                new HelpFormatter().printHelp("CommonsGeneticSearch", options);
            } else {
                execute(command);
            }
        } catch (ParseException e) {
            System.err.println("Command line parsing failed. Reason: " + e.getMessage());
        }
    }

    private static void execute(CommandLine command) {
        // Create the runtime factory.
        long seed = 1511535016195L;
        if (command.hasOption("seed")) {
            seed = System.currentTimeMillis();
        }

        final YapRuntimeFactory factory = MainUtil.createYapRuntimeFactory(command, seed);

        // Create the boolean parity problem.
        int parity = 12;
        if (command.hasOption("parity")) {
            parity = Integer.parseInt(command.getOptionValue("parity"));
        }

        final EvaluationStrategy evaluation = new BooleanParityProblem(factory.create(), parity);
        final ChromosomeFactory chromosomeFactory = new CommonsChromosomeFactoryImpl(evaluation);
        final CommonsGeneticAlgorithmBuilder builder = MainUtil.createBuilder(command, chromosomeFactory);

        int maxGenerations = 100;
        if (command.hasOption("max-generations")) {
            maxGenerations = Integer.parseInt(command.getOptionValue("max-generations"));
        }
        float maxFitness = .99995f;
        if (command.hasOption("max-fitness")) {
            maxFitness = Float.parseFloat(command.getOptionValue("max-fitness"));
        }

        // Print an overview of the settings of this run.
        String overview = "Settings for this run:\n" +
                "dry-run:\t\t" + command.hasOption("dry-run") + "\n" +
                "seed:\t\t\t" + seed + "L\n" +
                "parity:\t\t\t" + parity + " (2^" + parity + "=" + ((int) Math.pow(2, parity)) + ")\n" +
                "max-points:\t\t" + factory.getMaximumProgramPoints() + "\n" +
                "max-instructions:\t" + factory.getMaximumExecutionInstructions() + "\n" +
                "population-size:\t" + builder.getPopulationLimit() + "\n" +
                "tournament-arity:\t" + builder.getTournamentArity() + "\n" +
                "elitism-rate:\t\t" + builder.getElitismRate() + "\n" +
                "crossover-rate:\t\t" + builder.getCrossoverRate() + "\n" +
                "mutation-rate:\t\t" + builder.getMutationRate() + "\n" +
                "max-generation:\t\t" + maxGenerations + "\n" +
                "max-fitness:\t\t" + maxFitness;
        System.out.println(overview);

        if (!command.hasOption("dry-run")) {
            System.out.println("Starting run (please wait for output or press ^C to exit) ...\n");
            EvolutionHistory history = runOnce(maxGenerations, maxFitness, builder);
            log.info("Evolution history contains " + history.getSuccessiveWinners().size() + " successive improvements.");
        }
    }

    private static EvolutionHistory runOnce(int maxGenerations, float maxFitness, CommonsGeneticAlgorithmBuilder builder) {
        final EvolutionHistory history = new EvolutionHistory();
        final CommonsStopCondition stopCondition = new CommonsStopCondition(maxGenerations, maxFitness);
        stopCondition.setCallback(new CommonsStopCondition.Listener() {
            @Override
            public void notify(int generation, boolean maximumFitnessAttained, Population population) {
                final CommonsChromosome best = (CommonsChromosome) population.getFittestChromosome();
                if (generation % 10 == 0) {
                    String display = abbreviate(String.valueOf(best.getProgram()), 100) + " fitness=" + best.getFitness();
                    log.info("Generation num=" + generation + " produced " + display + ".");
                }
                history.addGenerationFittest(best);
                if (maximumFitnessAttained) {
                    log.info("Attained maximum fitness, no further generations will be processed.");
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("The overall best program found in this run is " + history.getBestChromosome() + ".")));
        Pair<GeneticAlgorithm, Population> pair = builder.build();
        GeneticAlgorithm algorithm = pair.getFirst();
        Population population = pair.getSecond();
        algorithm.evolve(population, stopCondition);
        return history;
    }
}
