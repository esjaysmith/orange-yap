package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: sjsmit
 * Date: 04/08/15
 * Time: 00:18
 */
public class InterpreterImpl implements Interpreter {

    private final Map<Region, Processor> processors;

    public InterpreterImpl(int codeProcessorMaxElements) {
        this.processors = new HashMap<>();
        processors.put(Region.BOOLEAN, new BooleanProcessor(this));
        processors.put(Region.INTEGER, new IntegerProcessor(this));
        processors.put(Region.FLOAT, new FloatProcessor(this));
        processors.put(Region.NAME, new NameProcessor(this));
        processors.put(Region.EXEC, new ExecProcessor(this));
        processors.put(Region.CODE, new CodeProcessor(this, codeProcessorMaxElements));
    }

    @Override
    public void run(MachineInternal m, Program p) {
        execute(m, p);
        while (m.exec().size() > 0 && m.context().doContinue()) {
            execute(m, m.exec().pop());
        }
    }

    @Override
    public void run(MachineInternal m) {
        run(m, m.code().peek());
    }

    @Override
    public void execute(MachineInternal m, Program p) {
        executeInternal(m, p);
    }

    /**
     * To execute program P:
     * Push P onto the EXEC stack
     * LOOP until the EXEC stack is empty:
     * If the first item on the EXEC stack is a single instruction
     * then pop it and execute it.
     * Else if the first item on the EXEC stack is a literal
     * then pop it and push it onto the appropriate stack.
     * Else (the first item must be a list) pop it and push all of the
     * items that it contains back onto the EXEC stack individually,
     * in reverse order (so that the item that was first in the list
     * ends up on top).
     */
    private void executeInternal(MachineInternal m, Program p) {
        m.context().onInstruction(p);
        if (!m.context().doContinue()) {
            return;
        }

        if (p instanceof Literal) {
            push(m, (Literal) p);
        } else if (p instanceof Instruction) {
            process(m, (Instruction) p);
        } else if (p instanceof Sequence) {
            List<Program> elements = ((Sequence) p).getElements();
            if (elements.isEmpty()) {
                return;
            }

            final int size = elements.size();
            for (int i = size - 1; i >= 0; i--) {
                final Program e = elements.get(i);
                m.exec().push(e);
            }
        }
    }

    private void push(MachineInternal m, Literal<?> literal) {
        switch (literal.getRegion()) {
            case BOOLEAN:
                m.booleans().push((Boolean) literal.getConstant());
                break;
            case FLOAT:
                m.floats().push((Float) literal.getConstant());
                break;
            case INTEGER:
                m.integers().push((Integer) literal.getConstant());
                break;
            case NAME:
                // If a name has not previously been given a value then it is pushed onto the NAME stack
                // when it is encountered.
                final String name = (String) literal.getConstant();
                final Program p = m.bindings().get(name);
                if (p == null) {
                    m.names().push(name);
                    break;
                }

                // A NAME.QUOTE instruction is provided to move names that already have definitions back onto the
                // NAME stack, presumably for the sake of re-definition. When NAME.QUOTE is executed a flag is set
                // that causes the next encountered name to be pushed onto the NAME stack, whether or not it has
                // previously been bound (and the flag is then cleared, whether or not the name was previously bound).
                if (m.bindings().isQuoting()) {
                    m.names().push(name);
                    m.bindings().setQuoting(false);
                    break;
                }

                // From that point forward the name will act as an instruction which, when executed, will push the
                // bound value onto the EXEC stack.
                m.exec().push(p);
                break;
            default:
                throw new IllegalArgumentException("Unknown literal " + literal + ".");
        }
    }

    @SuppressWarnings("unchecked")
    private void process(MachineInternal m, Instruction instruction) {
        Processor processor = processors.get(instruction.getRegion());
        if (processor == null) {
            throw new IllegalArgumentException(String.valueOf(instruction));
        }
        processor.process(m, instruction);
    }
}
