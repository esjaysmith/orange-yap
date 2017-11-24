package net.orange.yap.machine;

import net.orange.yap.machine.code.CodeGenerator;
import net.orange.yap.machine.stack.Program;

/**
 * The primary interface for obtaining and executing Machines.
 * <p>
 * The CodeGenerator is used to obtain new or combined Programs based on randomisation.
 * <p>
 * User: sjsmit
 * Date: 03/08/15
 * Time: 23:59
 *
 * @see CodeGenerator
 * @see YapRuntimeFactory
 * @see Machine
 * @see Program
 */
public interface YapRuntime {

    /**
     * Obtain the CodeGenerator of this runtime.
     *
     * @return This runtime's CodeGenerator.
     */
    CodeGenerator generator();

    /**
     * Creates the Machine version of a Program.
     *
     * @param program The Program to interpret.
     * @return A Machine instance which can be used to execute the Program.
     */
    Machine createMachine(Program program);

    /**
     * Obtain a new builder instance.
     *
     * @return A new MachineBuilder instance.
     */
    MachineBuilder createMachineBuilder();

    /**
     * Obtain a Program instance from the default String representation for Programs.
     *
     * @param code a Program in String format.
     * @return The Progam instance.
     */
    Program parseProgram(String code);

    /**
     * Serialize the Program to its default String representation.
     *
     * @param p The Program.
     * @return The default String representation.
     */
    String asString(Program p);

    /**
     * Run the machine until completion.
     */
    void execute(Machine m);
}
