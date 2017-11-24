package net.orange.yap.machine;

import net.orange.yap.machine.stack.Program;

/**
 * Used by the runtime during machine execution to determine whether continuation is desired.
 * <p>
 * User: sjsmit
 * Date: 03/08/15
 * Time: 23:58
 *
 * @see YapRuntime
 * @see Machine
 */
public interface ExecutionContext {

    /**
     * Aborts execution when continuation is not desired.
     *
     * @return True to continue execution, false otherwise.
     */
    boolean doContinue();

    /**
     * Called each time an instruction or sequence is executed.
     *
     * @param p The instruction which will be executed next.
     */
    default void onInstruction(Program p) {
    }
}
