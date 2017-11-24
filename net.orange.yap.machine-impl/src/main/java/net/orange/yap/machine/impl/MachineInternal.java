package net.orange.yap.machine.impl;

import net.orange.yap.machine.ExecutionContext;
import net.orange.yap.machine.Machine;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Stack;

/**
 * User: sjsmit
 * Date: 02/07/15
 * Time: 20:38
 */
public interface MachineInternal extends Machine {

    void initRandomNumberGenerator();

    ExecutionContext context();

    NameBindings bindings();

    Stack<String> names();

    Stack<Program> exec();

    Stack<Program> code();

    boolean randBoolean();

    int randInt(int bound);

    float randFloat();
}
