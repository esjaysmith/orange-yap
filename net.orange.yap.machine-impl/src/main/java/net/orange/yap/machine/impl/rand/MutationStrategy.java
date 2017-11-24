package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 11:20
 */
public interface MutationStrategy {

    Program generate();

    Program mutate(Program program);
}
