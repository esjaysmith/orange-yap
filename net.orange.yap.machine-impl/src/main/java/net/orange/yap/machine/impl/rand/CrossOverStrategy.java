package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 11:20
 */
public interface CrossOverStrategy {

    Program perform(Program program, Program with);
}
