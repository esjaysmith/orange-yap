package net.orange.yap.machine.impl;

import net.orange.yap.machine.ExecutionContext;
import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 04/08/15
 * Time: 22:49
 */
public class ExecutionContextImpl implements ExecutionContext {

    private final long maximumInstructions;
    private long instructions;

    public ExecutionContextImpl(long maximumInstructions) {
        this.maximumInstructions = maximumInstructions;
    }

    public long getInstructions() {
        return instructions;
    }

    @Override
    public boolean doContinue() {
        return instructions < maximumInstructions;
    }

    @Override
    public void onInstruction(Program program) {
        instructions++;
    }
}
