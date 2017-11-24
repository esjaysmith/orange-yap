package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.Stack;
import net.orange.yap.machine.stack.instruction.IntegerInstruction;
import net.orange.yap.machine.stack.literal.IntegerLiteral;

/**
 * User: sjsmit
 * Date: 26/07/15
 * Time: 11:59
 */
class IntegerProcessor extends AbstractProcessor<Integer> implements Processor<IntegerInstruction> {

    public IntegerProcessor(Interpreter interpreter) {
        super(interpreter);
    }

    @Override
    public void process(MachineInternal md, IntegerInstruction literal) {
        final Stack<Integer> integers = md.integers();
        switch (literal) {
            case EQUALS:
                super.equals(md.booleans(), integers);
                break;
            case DUP:
                super.duplicate(integers);
                break;
            case FLUSH:
                integers.clear();
                break;
            case POP:
                super.pop(integers);
                break;
            case ROT:
                super.rotate(integers);
                break;
            case SHOVE:
                super.shove(md.integers(), integers);
                break;
            case SWAP:
                super.swap(integers);
                break;
            case YANK:
                super.yank(md.integers(), integers);
                break;
            case YANK_DUP:
                super.yankdup(md.integers(), integers);
                break;
            case MODULO:
                // Pushes the second stack item modulo the top stack item.
                // If the top item is zero this acts as a no-op.
                if (integers.size() > 1 && integers.peek() != 0) {
                    int first = integers.pop();
                    int second = integers.pop();
                    integers.push(second % first);
                }
                break;
            case PRODUCT:
                // Pushes the product of the top two items.
                if (integers.size() > 1) {
                    int first = integers.pop();
                    int second = integers.pop();
                    integers.push(second * first);
                }
                break;
            case SUM:
                // Pushes the sum of the top two items.
                if (integers.size() > 1) {
                    int first = integers.pop();
                    int second = integers.pop();
                    integers.push(second + first);
                }
                break;
            case DIFFERENCE:
                // Pushes the difference of the top two items; that is, the second item minus the top item.
                if (integers.size() > 1) {
                    int first = integers.pop();
                    int second = integers.pop();
                    integers.push(second - first);
                }
                break;
            case QUOTIENT:
                // Pushes the quotient of the top two items; that is, the second item divided by the top item.
                // If the top item is zero this acts as a no-op.
                if (integers.size() > 1 && integers.peek() != 0) {
                    int first = integers.pop();
                    int second = integers.pop();
                    integers.push(second / first);
                }
                break;
            case LESS_THAN:
                // Pushes TRUE onto the BOOLEAN stack if the second item is less than the top item,
                // or FALSE otherwise.
                if (integers.size() > 1) {
                    int first = integers.pop();
                    int second = integers.pop();
                    md.booleans().push(second < first);
                }
                break;
            case GREATER_THAN:
                // Pushes TRUE onto the BOOLEAN stack if the second item is greater than the top item,
                // or FALSE otherwise.
                if (integers.size() > 1) {
                    int first = integers.pop();
                    int second = integers.pop();
                    md.booleans().push(second > first);
                }
                break;
            case DEFINE:
                // Defines the name on top of the NAME stack as an instruction that will push the top item
                // of the INTEGER stack onto the EXEC stack.
                if (integers.size() > 0 && md.names().size() > 0) {
                    md.bindings().define(md.names().pop(), new IntegerLiteral(integers.pop()));
                }
                break;
            case FROM_BOOLEAN:
                // Pushes 1 if the top BOOLEAN is TRUE, or 0 if the top BOOLEAN is FALSE.
                if (md.booleans().size() > 0) {
                    integers.push(md.booleans().pop() ? 1 : 0);
                }
                break;
            case FROM_FLOAT:
                // Pushes the result of truncating the top FLOAT.
                if (md.floats().size() > 0) {
                    // Typecasting truncates.
                    integers.push((int) md.floats().pop().floatValue());
                }
                break;
            case MAX:
                // Pushes the maximum of the top two items.
                if (integers.size() > 1) {
                    int first = integers.pop();
                    int second = integers.pop();
                    integers.push(second > first ? second : first);
                }
                break;
            case MIN:
                // Pushes the minimum of the top two items.
                if (integers.size() > 1) {
                    int first = integers.pop();
                    int second = integers.pop();
                    integers.push(second < first ? second : first);
                }
                break;
            case RAND:
                // Pushes a newly generated random INTEGER that is greater than or equal to MIN-RANDOM-INTEGER
                // and less than or equal to MAX-RANDOM-INTEGER.
                integers.push(md.randInt(10) * (md.randBoolean() ? 1 : -1));
                break;
            case STACK_DEPTH:
                // Pushes the stack depth onto the INTEGER stack (thereby increasing it).
                integers.push(integers.size());
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(literal));
        }
    }
}
