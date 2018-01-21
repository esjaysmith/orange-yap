package net.orange.yap.main.genetic.commons;

import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.stack.Program;
import org.apache.commons.math3.genetics.Chromosome;

/**
 * User: sjsmit
 * Date: 21/01/2018
 * Time: 20:51
 */
public interface ChromosomeFactory {

    YapRuntime getRuntime();

    Chromosome createChromosome();

    Chromosome createChromosome(Program program);
}
