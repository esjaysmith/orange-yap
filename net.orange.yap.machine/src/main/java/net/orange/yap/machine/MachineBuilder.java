package net.orange.yap.machine;

import net.orange.yap.machine.stack.Program;

/**
 * A builder interface to obtain a Machine from a Program or String representation.
 * <p>
 * A Machine not based on a Program defaults to noop (no-operation).
 * <p>
 * User: sjsmit
 * Date: 06/08/15
 * Time: 23:13
 *
 * @see Machine
 * @see Program
 * @see ExecutionContext
 */
public interface MachineBuilder {

    /**
     * Provided the Program as parsed entity.
     *
     * @param p The machine's program.
     * @return The builder instance.
     */
    MachineBuilder setProgram(Program p);

    /**
     * Provide the Program in String representation.
     *
     * @param code The machine's code.
     * @return The builder instance.
     */
    MachineBuilder setProgram(String code);

    /**
     * An context is required to have the Machine execute.
     *
     * @param context The execution context.
     * @return The builder instance.
     */
    MachineBuilder setContext(ExecutionContext context);

    /**
     * Builds the Machine.
     *
     * @return A new Machine instance according to your specifications.
     */
    Machine build();
}
