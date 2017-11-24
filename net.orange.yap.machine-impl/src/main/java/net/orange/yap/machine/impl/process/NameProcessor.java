package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.Stack;
import net.orange.yap.machine.stack.instruction.NameInstruction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: sjsmit
 * Date: 28/07/15
 * Time: 19:18
 */
class NameProcessor extends AbstractProcessor<String> implements Processor<NameInstruction> {

    public NameProcessor(Interpreter interpreter) {
        super(interpreter);
    }

    @Override
    public void process(MachineInternal md, NameInstruction literal) {
        final Stack<String> names = md.names();
        switch (literal) {
            case EQUALS:
                super.equals(md.booleans(), names);
                break;
            case DUP:
                super.duplicate(names);
                break;
            case FLUSH:
                names.clear();
                break;
            case POP:
                super.pop(names);
                break;
            case ROT:
                super.rotate(names);
                break;
            case SHOVE:
                super.shove(md.integers(), names);
                break;
            case SWAP:
                super.swap(names);
                break;
            case YANK:
                super.yank(md.integers(), names);
                break;
            case YANKDUP:
                super.yankdup(md.integers(), names);
                break;
            case QUOTE:
                // Sets a flag indicating that the next name encountered will be pushed onto the NAME stack
                // (and not have its associated value pushed onto the EXEC stack), regardless of whether or not
                // it has a definition. Upon encountering such a name and pushing it onto the NAME stack the flag
                // will be cleared (whether or not the pushed name had a definition).
                md.bindings().setQuoting(true);
                break;
            case RAND:
                // Pushes a newly generated random NAME.
                names.push(generateUniqueName(md));
                break;
            case RANDBOUNDNAME:
                // Pushes a randomly selected NAME that already has a definition.
                if (!md.bindings().isEmpty()) {
                    List<String> list = new ArrayList<>(md.bindings().listNames());
                    names.push(list.get(md.randInt(list.size())));
                }
                break;
            case STACKDEPTH:
                // Pushes the stack depth onto the INTEGER stack.
                md.integers().push(names.size());
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(literal));
        }
    }

    private String generateUniqueName(MachineInternal md) {
        Set<String> names = new HashSet<>(md.bindings().listNames());
        while (true) {
            String candidate = "rand-" + md.randInt((int) 1e9);
            if (!names.contains(candidate)) {
                return candidate;
            }
        }
    }
}
