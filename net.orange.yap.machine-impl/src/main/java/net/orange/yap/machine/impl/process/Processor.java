package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.Instruction;

/**
 * Implementations must be stateless and thread-safe.
 * <p>
 * User: sjsmit
 * Date: 01/07/15
 * Time: 15:15
 */
interface Processor<T extends Instruction> {

    void process(MachineInternal m, T literal);
}
