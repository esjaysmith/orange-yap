package net.orange.yap.machine.impl;

import net.orange.yap.machine.ExecutionContext;
import net.orange.yap.machine.Machine;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Stack;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * User: sjsmit
 * Date: 01/07/15
 * Time: 13:28
 */
class MachineImpl implements MachineInternal, Machine, Serializable {

    private final ExecutionContext context;
    private final BindingsImpl bindings;
    private final Stack<Program> code;
    private final Stack<Program> exec;
    private final Stack<Boolean> booleans;
    private final Stack<Integer> integers;
    private final Stack<Float> floats;
    private final Stack<String> names;

    private Random random;

    MachineImpl(int stackMaxDepth, ExecutionContext context, Map<String, Program> initial) {
        this.context = context;
        this.bindings = new BindingsImpl(initial);
        this.code = new StackImpl<>(stackMaxDepth);
        this.exec = new StackImpl<>(stackMaxDepth);
        this.booleans = new StackImpl<>(stackMaxDepth);
        this.integers = new StackImpl<>(stackMaxDepth);
        this.floats = new SafeFloatStackImpl(stackMaxDepth);
        this.names = new StackImpl<>(stackMaxDepth);
    }

    @Override
    public boolean randBoolean() {
        return random.nextBoolean();
    }

    @Override
    public int randInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public float randFloat() {
        return random.nextFloat();
    }

    @Override
    public ExecutionContext context() {
        return context;
    }

    @Override
    public NameBindings bindings() {
        return bindings;
    }

    @Override
    public Stack<Boolean> booleans() {
        return booleans;
    }

    @Override
    public Stack<Integer> integers() {
        return integers;
    }

    @Override
    public Stack<Float> floats() {
        return floats;
    }

    @Override
    public Stack<String> names() {
        return names;
    }

    @Override
    public Stack<Program> exec() {
        return exec;
    }

    @Override
    public Stack<Program> code() {
        return code;
    }

    @Override
    public List<Program> listExecutionItems() {
        return exec.toList();
    }

    @Override
    public List<Program> listCodeItems() {
        return code.toList();
    }

    @Override
    public void initRandomNumberGenerator() {
        // Setup the generator in a deterministic way to obtain repeatable behavior across runs.
        final float seed = floats.size() > 0 ? floats.pop() : 1f;
        random = new Random(new Double(seed * 1234000000000L).longValue());
    }
}
