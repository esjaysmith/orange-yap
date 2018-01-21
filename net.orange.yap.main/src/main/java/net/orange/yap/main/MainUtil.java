package net.orange.yap.main;

import net.orange.yap.machine.YapRuntimeFactory;
import net.orange.yap.machine.impl.YapRuntimeFactoryImpl;
import net.orange.yap.main.genetic.commons.ChromosomeFactory;
import net.orange.yap.main.genetic.commons.CommonsGeneticAlgorithmBuilder;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * User: sjsmit
 * Date: 20/01/2018
 * Time: 21:36
 */
class MainUtil {

    static void addBasicOptions(Options options) {
        options.addOption("h", "help", false, "Print this message");
        options.addOption("d", "dry-run", false, "Show settings but do not start a run");
        options.addOption("s", "seed", false, "Use a different randomisation seed for each run");
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
    }

    static YapRuntimeFactory createYapRuntimeFactory(CommandLine command) {
        return createYapRuntimeFactory(command, System.currentTimeMillis());
    }

    static YapRuntimeFactory createYapRuntimeFactory(CommandLine command, long seed) {
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
        return factory;
    }

    static CommonsGeneticAlgorithmBuilder createBuilder(CommandLine command, ChromosomeFactory factory) {
        // Construct the builder and supply the command line parameters.
        CommonsGeneticAlgorithmBuilder builder = new CommonsGeneticAlgorithmBuilder(factory);
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
        return builder;
    }
}
