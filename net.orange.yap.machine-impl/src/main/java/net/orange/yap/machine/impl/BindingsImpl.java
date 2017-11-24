package net.orange.yap.machine.impl;

import net.orange.yap.machine.stack.Program;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Global machine variables (shared processor state).
 * <p>
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:46
 */
class BindingsImpl implements NameBindings {

    private final Map<String, Program> nameBindings;
    private boolean quoting;

    public BindingsImpl(Map<String, Program> initial) {
        this.nameBindings = new HashMap<>(initial);
    }

    @Override
    public Program get(String name) {
        return nameBindings.get(name);
    }

    @Override
    public boolean isEmpty() {
        return nameBindings.isEmpty();
    }

    @Override
    public Collection<String> listNames() {
        return nameBindings.keySet();
    }

    @Override
    public void define(String name, Program p) {
        nameBindings.put(name, p);
    }

    @Override
    public boolean isQuoting() {
        return quoting;
    }

    @Override
    public void setQuoting(boolean quoting) {
        this.quoting = quoting;
    }
}
