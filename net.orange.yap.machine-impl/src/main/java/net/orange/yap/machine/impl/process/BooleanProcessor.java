package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.Stack;
import net.orange.yap.machine.stack.instruction.BooleanInstruction;
import net.orange.yap.machine.stack.literal.BooleanLiteral;

/**
 * Unless otherwise noted all instructions <strong>pop</strong> <i>all</i> arguments that they consult.
 * <p>
 * User: sjsmit
 * Date: 01/07/15
 * Time: 15:20
 */
class BooleanProcessor extends AbstractProcessor<Boolean> implements Processor<BooleanInstruction> {

    public BooleanProcessor(Interpreter interpreter) {
        super(interpreter);
    }

    @Override
    public void process(MachineInternal md, BooleanInstruction literal) {
        final Stack<Boolean> booleans = md.booleans();
        switch (literal) {
            case EQUALS:
                super.equals(booleans, booleans);
                break;
            case DUP:
                super.duplicate(booleans);
                break;
            case FLUSH:
                booleans.clear();
                break;
            case POP:
                super.pop(booleans);
                break;
            case ROT:
                super.rotate(booleans);
                break;
            case SHOVE:
                super.shove(md.integers(), booleans);
                break;
            case SWAP:
                super.swap(booleans);
                break;
            case YANK:
                super.yank(md.integers(), booleans);
                break;
            case YANK_DUP:
                super.yankdup(md.integers(), booleans);
                break;
            case AND:
                // Pushes the logical AND of the top two booleans.
                if (booleans.size() > 1) {
                    booleans.push(booleans.pop() & booleans.pop());
                }
                break;
            case DEFINE:
                // Defines the name on top of the NAME stack as an instruction that will push the top item of
                // the BOOLEAN stack onto the EXEC stack.
                if (booleans.size() > 0 && md.names().size() > 0) {
                    md.bindings().define(md.names().pop(), new BooleanLiteral(booleans.pop()));
                }
                break;
            case FROM_FLOAT:
                // Pushes FALSE if the top FLOAT is 0.0, or TRUE otherwise.
                if (md.floats().size() > 0) {
                    booleans.push(md.floats().pop() != 0f);
                }
                break;
            case FROM_INTEGER:
                // Pushes FALSE if the top INTEGER is 0, or TRUE otherwise.
                if (md.integers().size() > 0) {
                    booleans.push(md.integers().pop() != 0);
                }
                break;
            case NOT:
                // Pushes the logical NOT of the top boolean.
                if (booleans.size() > 0) {
                    booleans.push(!booleans.pop());
                }
                break;
            case OR:
                // Pushes the logical OR of the top two booleans.
                if (booleans.size() > 1) {
                    booleans.push(booleans.pop() | booleans.pop());
                }
                break;
            case RAND:
                // Pushes a random boolean.
                booleans.push(md.randBoolean());
                break;
            case STACK_DEPTH:
                // Pushes the stack depth onto the INTEGER stack.
                md.integers().push(booleans.size());
                break;
            case NAND:
                if (booleans.size() > 1) {
                    booleans.push(!(booleans.pop() & booleans.pop()));
                }
                break;
            case XOR:
                if (booleans.size() > 1) {
                    booleans.push(booleans.pop() ^ booleans.pop());
                }
                break;
            case NOR:
                if (booleans.size() > 1) {
                    booleans.push(!(booleans.pop() | booleans.pop()));
                }
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(literal));
        }
    }
}
