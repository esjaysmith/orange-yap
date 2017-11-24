package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 06/08/15
 * Time: 20:41
 */
public class RandomCodeGeneratorImpl implements net.orange.yap.machine.code.CodeGenerator {

    private final CrossOverStrategy crossover;
    private final MutationStrategy mutation;

    public RandomCodeGeneratorImpl(CrossOverStrategy crossover, MutationStrategy mutation) {
        this.crossover = crossover;
        this.mutation = mutation;
    }

    @Override
    public Program generate() {
        return mutation.generate();
    }

    @Override
    public Program combine(Program program, Program with) {
        return crossover.perform(program, with);
    }

    @Override
    public Program mutate(Program program) {
        return mutation.mutate(program);
    }
}
