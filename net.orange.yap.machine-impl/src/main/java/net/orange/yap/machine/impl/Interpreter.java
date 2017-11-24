package net.orange.yap.machine.impl;

import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 03/08/15
 * Time: 23:41
 */
public interface Interpreter {

    /**
     * Run the machine until completion. The program is copied from the code stack.
     */
    void run(MachineInternal m);

    /**
     * Run the provided program until completion.
     */
    void run(MachineInternal m, Program p);

    /**
     * Run just the one program.
     */
    void execute(MachineInternal m, Program p);
}
