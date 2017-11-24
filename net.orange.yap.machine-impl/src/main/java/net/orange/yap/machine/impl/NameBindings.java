package net.orange.yap.machine.impl;

import net.orange.yap.machine.stack.Program;

import java.util.Collection;

/**
 * Global machine variables (shared processor state).
 * <p>
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:46
 */
public interface NameBindings {

    void define(String name, Program p);

    Program get(String name);

    boolean isEmpty();

    Collection<String> listNames();

    boolean isQuoting();

    void setQuoting(boolean quoting);
}
