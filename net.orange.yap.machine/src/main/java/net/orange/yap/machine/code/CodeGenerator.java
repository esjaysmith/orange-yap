package net.orange.yap.machine.code;

import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 06/08/15
 * Time: 23:20
 */
public interface CodeGenerator {

    Program generate();

    Program combine(Program original, Program with);

    Program mutate(Program original);
}
