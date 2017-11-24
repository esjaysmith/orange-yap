package net.orange.yap.machine;

import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Stack;

import java.util.List;

/**
 * A Machine is the interpreted instance of a Program and can be executed by a YapRuntime.
 * This interface allows end-users read and write access to the stacks.
 * <p>
 * Implementations are not required to be thread-safe.
 * <p>
 * User: sjsmit
 * Date: 03/08/15
 * Time: 23:56
 *
 * @see Program
 * @see Stack
 * @see YapRuntime
 */
public interface Machine {

    Stack<Boolean> booleans();

    Stack<Integer> integers();

    Stack<Float> floats();

    List<Program> listExecutionItems();

    List<Program> listCodeItems();
}
