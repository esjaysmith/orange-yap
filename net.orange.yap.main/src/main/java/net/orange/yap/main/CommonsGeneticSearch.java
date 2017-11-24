package net.orange.yap.main;

import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.machine.impl.YapRuntimeFactoryImpl;
import net.orange.yap.main.evaluation.BooleanParityProblem;
import net.orange.yap.main.genetic.commons.CommonsChromosome;
import net.orange.yap.main.genetic.commons.CommonsGeneticAlgorithmBuilder;
import net.orange.yap.main.genetic.commons.CommonsStopCondition;
import net.orange.yap.main.genetic.commons.EvolutionHistory;
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
        options.addOption("h", "help", false, "Print this message");
        options.addOption("d", "dry-run", false, "Show settings but do not start a run");
        options.addOption("s", "seed", false, "Use a different randomisation seed for each run");
        options.addOption(Option.builder().longOpt("parity")
                .desc("The parity of the boolean parity problem the genetic algorithm is to solve")
                .hasArg()
                .argName("LEN")
                .build());
        options.addOption(Option.builder().longOpt("max-points")
                .desc("The maximum length of a program in points")
                .hasArg()
                .argName("NUM")
                .build());
        options.addOption(Option.builder().longOpt("max-instructions")
                .desc("The maximum amount of instructions a machine is allowed to execute as a program")
                .hasArg()
                .argName("NUM")
                .build());
        options.addOption(Option.builder().longOpt("population-size")
                .desc("Size of the population (number of individuals)")
                .hasArg()
                .argName("SIZE")
                .build());
        options.addOption(Option.builder().longOpt("tournament-arity")
                .desc("Number of individuals per group during tournament selection")
                .hasArg()
                .argName("ARITY")
                .build());
        options.addOption(Option.builder().longOpt("elitism-rate")
                .desc("Determines the rate of elitism")
                .hasArg()
                .argName("RATE")
                .build());
        options.addOption(Option.builder().longOpt("crossover-rate")
                .desc("Determines the rate of crossover")
                .hasArg()
                .argName("RATE")
                .build());
        options.addOption(Option.builder().longOpt("mutation-rate")
                .desc("Determines the rate of mutation")
                .hasArg()
                .argName("RATE")
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

        // Create the boolean parity problem.
        int parity = 12;
        if (command.hasOption("parity")) {
            parity = Integer.parseInt(command.getOptionValue("parity"));
        }
        int maxPoints = 50;
        if (command.hasOption("max-points")) {
            maxPoints = Integer.parseInt(command.getOptionValue("max-points"));
        }
        int maxInstructions = 50;
        if (command.hasOption("max-instructions")) {
            maxInstructions = Integer.parseInt(command.getOptionValue("max-instructions"));
        }

        YapRuntimeFactoryImpl factory = new YapRuntimeFactoryImpl(seed);
        factory.setMaximumProgramPoints(maxPoints);
        factory.setMaximumExecutionInstructions(maxInstructions);

        final EvaluationStrategy evaluation = new BooleanParityProblem(factory.create(), parity);

        // Construct the builder and supply the command line parameters.
        CommonsGeneticAlgorithmBuilder builder = new CommonsGeneticAlgorithmBuilder(evaluation);
        if (command.hasOption("population-size")) {
            builder = builder.setPopulationLimit(Integer.parseInt(command.getOptionValue("population-size")));
        }
        if (command.hasOption("tournament-arity")) {
            builder = builder.setTournamentArity(Integer.parseInt(command.getOptionValue("tournament-arity")));
        }
        if (command.hasOption("elitism-rate")) {
            builder = builder.setElitismRate(Float.parseFloat(command.getOptionValue("elitism-rate")));
        }
        if (command.hasOption("crossover-rate")) {
            builder = builder.setCrossoverRate(Float.parseFloat(command.getOptionValue("crossover-rate")));
        }
        if (command.hasOption("mutation-rate")) {
            builder = builder.setMutationRate(Float.parseFloat(command.getOptionValue("mutation-rate")));
        }

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
                "max-points:\t\t" + maxPoints + "\n" +
                "max-instructions:\t" + maxInstructions + "\n" +
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
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("The overall best program found in this run is " + history.getBestChromosome() + ".")));

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

        Pair<GeneticAlgorithm, Population> pair = builder.build();
        pair.getFirst().evolve(pair.getSecond(), stopCondition);
        return history;
    }
}
