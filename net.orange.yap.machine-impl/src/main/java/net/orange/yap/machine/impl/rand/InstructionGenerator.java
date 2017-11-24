package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 11:32
 */
public interface InstructionGenerator {

    Instruction generate();

    Program generate(int points);
}
