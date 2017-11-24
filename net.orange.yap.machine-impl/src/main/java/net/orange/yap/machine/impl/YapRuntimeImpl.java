package net.orange.yap.machine.impl;

import net.orange.yap.machine.Machine;
import net.orange.yap.machine.MachineBuilder;
import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.code.CodeGenerator;
import net.orange.yap.machine.code.CodeParser;
import net.orange.yap.machine.code.CodeSerializer;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.instruction.CodeInstruction;
import net.orange.yap.machine.util.MachineUtils;

/**
 * User: sjsmit
 * Date: 04/08/15
 * Time: 23:11
 */
class YapRuntimeImpl implements YapRuntime {

    private final CodeParser parser;
    private final CodeSerializer serializer;
    private final Interpreter interpreter;
    private final CodeGenerator generator;
    private int maximumStackDepth;
    private int maximumProgramPoints;
    private int maximumExecutionInstructions;

    YapRuntimeImpl(CodeParser parser, CodeSerializer serializer, Interpreter interpreter, CodeGenerator generator) {
        this.parser = parser;
        this.serializer = serializer;
        this.interpreter = interpreter;
        this.generator = generator;
    }

    void setMaximumStackDepth(int maximumStackDepth) {
        this.maximumStackDepth = maximumStackDepth;
    }

    void setMaximumProgramPoints(int maximumProgramPoints) {
        this.maximumProgramPoints = maximumProgramPoints;
    }

    void setMaximumExecutionInstructions(int maximumExecutionInstructions) {
        this.maximumExecutionInstructions = maximumExecutionInstructions;
    }

    @Override
    public CodeGenerator generator() {
        return generator;
    }

    @Override
    public Machine createMachine(Program program) {
        if (MachineUtils.countPoints(program) > maximumProgramPoints) {
            program = CodeInstruction.NOOP;
        }
        return new MachineBuilderImpl(maximumStackDepth, parser)
                .setProgram(program)
                .setContext(new ExecutionContextImpl(maximumExecutionInstructions))
                .build();
    }

    @Override
    public MachineBuilder createMachineBuilder() {
        return new MachineBuilderImpl(maximumStackDepth, parser);
    }

    @Override
    public Program parseProgram(String code) {
        return parser.fromString(code);
    }

    @Override
    public String asString(Program p) {
        return serializer.asString(p);
    }

    @Override
    public void execute(Machine m) {
        interpreter.run((MachineInternal) m);
    }
}
