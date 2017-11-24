package net.orange.yap.machine;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Stack;
import net.orange.yap.machine.stack.instruction.BooleanInstruction;

import java.util.Arrays;

/**
 * User: sjsmit
 * Date: 01/07/15
 * Time: 13:31
 */
public class TestBooleanStack extends AbstractRuntimeTests {

    public void testData() throws Exception {
        Machine machine = rt.createMachine(null);
        Stack<Boolean> stack = machine.booleans();
        assertEquals(0, stack.size());
        assertTrue(Arrays.equals(new Boolean[]{}, stack.toArray()));

        // Data is in stack order.
        stack.push(false);
        assertEquals(1, stack.size());
        assertEquals(Boolean.FALSE, stack.peek());
        assertTrue(Arrays.equals(new Boolean[]{false}, stack.toArray()));

        stack.clear();
        stack.push(true);
        stack.push(true);
        stack.push(false);

        assertEquals(3, stack.size());
        assertEquals(Boolean.FALSE, stack.peek());
        assertTrue(Arrays.equals(new Boolean[]{false, true, true}, stack.toArray()));

        assertEquals(Boolean.FALSE, stack.pop());
        assertTrue(Arrays.equals(new Boolean[]{true, true}, stack.toArray()));
    }

    public void testNoop() throws Exception {
        Machine machine = rt.createMachine(BooleanInstruction.NOT);
        rt.execute(machine);
        rt.execute(machine);
        assertEquals(0, machine.listExecutionItems().size());
        assertEquals(0, machine.booleans().size());

        // Strictly no-op so that must also leave the stack untouched.
        machine = rt.createMachine(BooleanInstruction.AND);
        machine.booleans().push(true);
        rt.execute(machine);
        assertEquals(0, machine.listExecutionItems().size());
        assertEquals(1, machine.booleans().size());
        assertTrue(Arrays.equals(new Boolean[]{true}, machine.booleans().toArray()));
    }

    public void testFunctions() throws Exception {
        //
        // EQUALS
        //
        twoParameters(false, false, BooleanInstruction.EQUALS, true);
        twoParameters(false, true, BooleanInstruction.EQUALS, false);
        twoParameters(true, false, BooleanInstruction.EQUALS, false);
        twoParameters(true, true, BooleanInstruction.EQUALS, true);

        //
        // AND
        //
        twoParameters(false, false, BooleanInstruction.AND, false);
        twoParameters(false, true, BooleanInstruction.AND, false);
        twoParameters(true, false, BooleanInstruction.AND, false);
        twoParameters(true, true, BooleanInstruction.AND, true);

        //
        // DEFINE
        //

        //
        // DUP
        //
        oneParameter(false, BooleanInstruction.DUP, false);
        oneParameter(true, BooleanInstruction.DUP, true);

        Machine machine = rt.createMachine(BooleanInstruction.DUP);
        machine.booleans().push(false);
        rt.execute(machine);
        assertEquals(0, machine.listExecutionItems().size());
        assertEquals(2, machine.booleans().size());
        assertTrue(Arrays.equals(new Boolean[]{false, false}, machine.booleans().toArray()));

        machine.booleans().clear();
        assertEquals(0, machine.booleans().size());
        assertEquals(0, machine.listExecutionItems().size());

        machine = rt.createMachine(BooleanInstruction.DUP);
        machine.booleans().push(true);
        rt.execute(machine);
        assertEquals(0, machine.listExecutionItems().size());
        assertEquals(2, machine.booleans().size());
        assertTrue(Arrays.equals(new Boolean[]{true, true}, machine.booleans().toArray()));

        //
        // FLUSH
        //
        machine = rt.createMachine(BooleanInstruction.FLUSH);
        assertEquals(0, machine.booleans().size());
        assertEquals(0, machine.listExecutionItems().size());
        machine.booleans().push(false);
        machine.booleans().push(true);
        assertEquals(2, machine.booleans().size());
        rt.execute(machine);
        assertEquals(0, machine.booleans().size());
        assertEquals(0, machine.listExecutionItems().size());

        //
        // FROM-FLOAT
        //
        machine = rt.createMachine(rt.parseProgram("( BOOLEAN.FROM-FLOAT BOOLEAN.FROM-FLOAT )"));
        machine.floats().push(0f);
        machine.floats().push(0.1f);
        rt.execute(machine);
        assertEquals(2, machine.booleans().size());
        assertFalse(machine.booleans().pop());
        assertTrue(machine.booleans().peek());

        //
        // FROM-INTEGER
        //
        machine = rt.createMachine(rt.parseProgram("(BOOLEAN.FROM-INTEGER BOOLEAN.FROM-INTEGER)"));
        machine.integers().push(0);
        machine.integers().push(1);
        rt.execute(machine);
        assertEquals(2, machine.booleans().size());
        assertFalse(machine.booleans().pop());
        assertTrue(machine.booleans().peek());

        //
        // NOT
        //
        oneParameter(false, BooleanInstruction.NOT, true);
        oneParameter(true, BooleanInstruction.NOT, false);

        //
        // OR
        //
        twoParameters(false, false, BooleanInstruction.OR, false);
        twoParameters(false, true, BooleanInstruction.OR, true);
        twoParameters(true, false, BooleanInstruction.OR, true);
        twoParameters(true, true, BooleanInstruction.OR, true);

        //
        // POP
        //
        machine = rt.createMachine(BooleanInstruction.POP);
        machine.booleans().push(false);
        machine.booleans().push(true);
        rt.execute(machine);
        assertEquals(0, machine.listExecutionItems().size());
        assertTrue(Arrays.equals(new Boolean[]{false}, machine.booleans().toArray()));

        //
        // RAND
        //
        /*machine = rt.createMachine(BooleanInstruction.RAND);
        assertEquals(0, machine.booleans().size());
        rt.execute(machine);
        assertEquals(1, machine.booleans().size());*/

        //
        // ROT
        //
        machine = rt.createMachine(BooleanInstruction.ROT);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, true, false}, machine.booleans().toArray()));

        //
        // SHOVE
        //
        machine = rt.createMachine(BooleanInstruction.SHOVE);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true}, machine.booleans().toArray()));

        machine = rt.createMachine(BooleanInstruction.SHOVE);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(-1);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.SHOVE);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(0);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.SHOVE);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(1);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, true, true}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.SHOVE);
        machine.booleans().push(true);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.integers().push(2);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.SHOVE);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(true);
        machine.integers().push(3);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.SHOVE);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(4);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, true, true}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        //
        // STACK-DEPTH
        //
        machine = rt.createMachine(BooleanInstruction.STACK_DEPTH);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        assertEquals(0, machine.integers().size());
        rt.execute(machine);
        assertEquals(1, machine.integers().size());
        assertEquals((Integer) 3, machine.integers().peek());

        //
        // SWAP
        //
        machine = rt.createMachine(BooleanInstruction.SWAP);
        machine.booleans().push(true);
        machine.booleans().push(false);
        rt.execute(machine);
        assertEquals(0, machine.listExecutionItems().size());
        assertTrue(machine.booleans().pop());
        assertFalse(machine.booleans().pop());
        assertEquals(0, machine.booleans().size());

        //
        // YANK
        //
        machine = rt.createMachine(BooleanInstruction.YANK);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true, false}, machine.booleans().toArray()));

        machine = rt.createMachine(BooleanInstruction.YANK);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(-1);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.YANK);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(0);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.YANK);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(1);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, true, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.YANK);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.integers().push(2);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.YANK);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.integers().push(3);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, true, false, true}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.YANK);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.integers().push(4);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{true, false, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());


        //
        // YANK-DUP
        //
        machine = rt.createMachine(BooleanInstruction.YANK_DUP);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, true, false}, machine.booleans().toArray()));

        machine = rt.createMachine(BooleanInstruction.YANK_DUP);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.integers().push(-1);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, false, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.YANK_DUP);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(false);
        machine.integers().push(0);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, false, false, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        machine = rt.createMachine(BooleanInstruction.YANK_DUP);
        machine.booleans().push(false);
        machine.booleans().push(true);
        machine.booleans().push(false);
        machine.booleans().push(false);
        machine.booleans().push(false);
        machine.booleans().pop();
        machine.booleans().pop();
        machine.integers().push(3);
        rt.execute(machine);
        assertTrue(Arrays.equals(new Boolean[]{false, false, true, false}, machine.booleans().toArray()));
        assertEquals(0, machine.integers().size());

        //
        // XOR
        //
        twoParameters(false, false, BooleanInstruction.XOR, false);
        twoParameters(false, true, BooleanInstruction.XOR, true);
        twoParameters(true, false, BooleanInstruction.XOR, true);
        twoParameters(true, true, BooleanInstruction.XOR, false);
        //
        // NOR
        //
        twoParameters(false, false, BooleanInstruction.NOR, true);
        twoParameters(false, true, BooleanInstruction.NOR, false);
        twoParameters(true, false, BooleanInstruction.NOR, false);
        twoParameters(true, true, BooleanInstruction.NOR, false);

        //
        // NAND
        //
        twoParameters(false, false, BooleanInstruction.NAND, true);
        twoParameters(false, true, BooleanInstruction.NAND, true);
        twoParameters(true, false, BooleanInstruction.NAND, true);
        twoParameters(true, true, BooleanInstruction.NAND, false);
    }

    private void oneParameter(boolean first, Instruction function, boolean expected) {
        Machine machine = rt.createMachine(function);
        machine.booleans().push(first);
        rt.execute(machine);
        assertEquals(Boolean.valueOf(expected), machine.booleans().peek());
    }

    private void twoParameters(boolean first, boolean second, Instruction function, boolean expected) {
        Machine machine = rt.createMachine(function);
        machine.booleans().push(first);
        machine.booleans().push(second);
        rt.execute(machine);
        assertEquals(Boolean.valueOf(expected), machine.booleans().peek());
    }
}
