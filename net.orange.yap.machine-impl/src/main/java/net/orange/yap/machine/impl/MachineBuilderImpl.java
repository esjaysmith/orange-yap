package net.orange.yap.machine.impl;

import net.orange.yap.machine.ExecutionContext;
import net.orange.yap.machine.Machine;
import net.orange.yap.machine.MachineBuilder;
import net.orange.yap.machine.code.CodeParser;
import net.orange.yap.machine.stack.Program;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * User: sjsmit
 * Date: 06/08/15
 * Time: 23:26
 */
class MachineBuilderImpl implements MachineBuilder {

    private final int stackMaxDepth;
    private final CodeParser parser;

    private Program program;
    private ExecutionContext context;
    private Map<String, Program> initialBindings;

    MachineBuilderImpl(int stackMaxDepth, CodeParser parser) {
        this.stackMaxDepth = stackMaxDepth;
        this.parser = parser;
    }

    @Override
    public MachineBuilder setProgram(Program p) {
        program = p;
        return this;
    }

    @Override
    public MachineBuilder setProgram(String code) {
        program = parser.fromString(code);
        return this;
    }

    @Override
    public MachineBuilder setContext(ExecutionContext context) {
        this.context = context;
        return this;
    }

    public MachineBuilder setInitialBindings(Map<String, Program> bindings) {
        this.initialBindings = bindings;
        return this;
    }

    @Override
    public Machine build() {
        Objects.requireNonNull(context, "An execution context must be provided first.");
        initialBindings = initialBindings == null ? Collections.emptyMap() : initialBindings;
        MachineImpl m = new MachineImpl(stackMaxDepth, context, initialBindings);
        m.initRandomNumberGenerator();
        m.code().push(program);
        return m;
    }
}
