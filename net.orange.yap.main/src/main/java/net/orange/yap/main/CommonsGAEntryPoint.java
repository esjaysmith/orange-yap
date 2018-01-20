package net.orange.yap.main;

import net.orange.yap.machine.YapRuntimeFactory;
import net.orange.yap.machine.eval.EvaluationStrategy;
import net.orange.yap.main.evaluation.RemoteEvaluationStrategy;
import net.orange.yap.main.genetic.commons.CommonsGeneticAlgorithmBuilder;
import net.orange.yap.main.genetic.commons.CommonsGeneticExperiment;
import org.apache.commons.cli.*;
import py4j.GatewayServer;

/**
 * User: sjsmit
 * Date: 20/01/2018
 * Time: 20:40
 */
public class CommonsGAEntryPoint {

    private final CommonsGeneticExperiment experiment;

    CommonsGAEntryPoint(CommonsGeneticAlgorithmBuilder builder) {
        this.experiment = new CommonsGeneticExperiment(builder);
    }

    public CommonsGeneticExperiment getExperiment() {
        return experiment;
    }

    public static void main(String[] args) {
        Options options = new Options();
        MainUtil.addBasicOptions(options);

        try {
            CommandLine command = new DefaultParser().parse(options, args);
            if (command.hasOption("help")) {
                new HelpFormatter().printHelp("CommonsGeneticSearch", options);
            } else {
                final YapRuntimeFactory factory = MainUtil.createYapRuntimeFactory(command);
                final EvaluationStrategy evaluation = new RemoteEvaluationStrategy(factory.create());
                final CommonsGeneticAlgorithmBuilder builder = MainUtil.createBuilder(command, evaluation);
                final GatewayServer gatewayServer = new GatewayServer(new CommonsGAEntryPoint(builder));
                String overview = "Gateway server for push machine evolution settings:\n" +
                        "dry-run:\t\t" + command.hasOption("dry-run") + "\n" +
                        "max-points:\t\t" + factory.getMaximumProgramPoints() + "\n" +
                        "max-instructions:\t" + factory.getMaximumExecutionInstructions() + "\n" +
                        "population-size:\t" + builder.getPopulationLimit() + "\n" +
                        "tournament-arity:\t" + builder.getTournamentArity() + "\n" +
                        "elitism-rate:\t\t" + builder.getElitismRate() + "\n" +
                        "crossover-rate:\t\t" + builder.getCrossoverRate() + "\n" +
                        "mutation-rate:\t\t" + builder.getMutationRate();
                System.out.println(overview);
                if (!command.hasOption("dry-run")) {
                    gatewayServer.start();
                    System.out.println("Server started.");
                }
            }
        } catch (ParseException e) {
            System.err.println("Command line parsing failed. Reason: " + e.getMessage());
        }

    }
}
