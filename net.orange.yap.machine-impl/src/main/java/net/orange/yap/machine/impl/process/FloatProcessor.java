package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.Stack;
import net.orange.yap.machine.stack.instruction.FloatInstruction;
import net.orange.yap.machine.stack.literal.FloatLiteral;
import org.apache.commons.math3.analysis.function.Gaussian;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static net.orange.yap.util.math.FloatUtils.sigmoid;

/**
 * User: sjsmit
 * Date: 26/07/15
 * Time: 11:59
 */
class FloatProcessor extends AbstractProcessor<Float> implements Processor<FloatInstruction> {

    private final Gaussian gaussianZeroMeanUnitStdDev;

    public FloatProcessor(Interpreter interpreter) {
        super(interpreter);
        this.gaussianZeroMeanUnitStdDev = new Gaussian();
    }

    @Override
    public void process(MachineInternal md, FloatInstruction literal) {
        final Stack<Float> floats = md.floats();
        switch (literal) {
            case EQUALS:
                super.equals(md.booleans(), floats);
                break;
            case DUP:
                super.duplicate(floats);
                break;
            case FLUSH:
                floats.clear();
                break;
            case POP:
                super.pop(floats);
                break;
            case ROT:
                super.rotate(floats);
                break;
            case SHOVE:
                super.shove(md.integers(), floats);
                break;
            case SWAP:
                super.swap(floats);
                break;
            case YANK:
                super.yank(md.integers(), floats);
                break;
            case YANKDUP:
                super.yankdup(md.integers(), floats);
                break;
            case MODULO:
                // Pushes the second stack item modulo the top stack item.
                // If the top item is zero this acts as a no-op.
                if (floats.size() > 1 && floats.peek() != 0f) {
                    float first = floats.pop();
                    float second = floats.pop();
                    floats.push(second % first);
                }
                break;
            case PRODUCT:
                // Pushes the product of the top two items.
                if (floats.size() > 1) {
                    float first = floats.pop();
                    float second = floats.pop();
                    floats.push(second * first);
                }
                break;
            case SUM:
                // Pushes the sum of the top two items.
                if (floats.size() > 1) {
                    float first = floats.pop();
                    float second = floats.pop();
                    floats.push(second + first);
                }
                break;
            case DIFFERENCE:
                // Pushes the difference of the top two items; that is, the second item minus the top item.
                if (floats.size() > 1) {
                    float first = floats.pop();
                    float second = floats.pop();
                    floats.push(second - first);
                }
                break;
            case QUOTIENT:
                // Pushes the quotient of the top two items; that is, the second item divided by the top item.
                // If the top item is zero this acts as a no-op.
                if (floats.size() > 1 && floats.peek() != 0f) {
                    float first = floats.pop();
                    float second = floats.pop();
                    floats.push(second / first);
                }
                break;
            case LESSTHAN:
                // Pushes TRUE onto the BOOLEAN stack if the second item is less than the top item,
                // or FALSE otherwise.
                if (floats.size() > 1) {
                    float first = floats.pop();
                    float second = floats.pop();
                    md.booleans().push(second < first);
                }
                break;
            case GREATERTHAN:
                // Pushes TRUE onto the BOOLEAN stack if the second item is greater than the top item,
                // or FALSE otherwise.
                if (floats.size() > 1) {
                    float first = floats.pop();
                    float second = floats.pop();
                    md.booleans().push(second > first);
                }
                break;
            case COS:
                // Pushes the cosine of the top item.
                if (floats.size() > 0) {
                    floats.push((float) Math.cos(floats.pop()));
                }
                break;
            case DEFINE:
                // Defines the name on top of the NAME stack as an instruction that will push the top item
                // of the FLOAT stack onto the EXEC stack.
                if (floats.size() > 0 && md.names().size() > 0) {
                    md.bindings().define(md.names().pop(), new FloatLiteral(floats.pop()));
                }
                break;
            case FROMBOOLEAN:
                // Pushes 1 if the top BOOLEAN is TRUE, or 0 if the top BOOLEAN is FALSE.
                if (md.booleans().size() > 0) {
                    boolean b = md.booleans().pop();
                    floats.push(b ? 1f : 0f);
                }
                break;
            case FROMINTEGER:
                // Pushes a floating point version of the top INTEGER.
                if (md.integers().size() > 0) {
                    floats.push(Float.valueOf(md.integers().pop()));
                }
                break;
            case MAX:
                // Pushes the maximum of the top two items.
                if (floats.size() > 1) {
                    float first = floats.pop();
                    float second = floats.pop();
                    floats.push(second > first ? second : first);
                }
                break;
            case MIN:
                // Pushes the minimum of the top two items.
                if (floats.size() > 1) {
                    float first = floats.pop();
                    float second = floats.pop();
                    floats.push(second < first ? second : first);
                }
                break;
            case RAND:
                // Pushes a newly generated random FLOAT that is greater than or equal to MIN-RANDOM-FLOAT
                // and less than or equal to MAX-RANDOM-FLOAT.
                floats.push(md.randInt(10) * (md.randBoolean() ? 1 : -1) * md.randFloat());
                break;
            case SIN:
                // Pushes the sine of the top item.
                if (floats.size() > 0) {
                    floats.push((float) Math.sin(floats.pop()));
                }
                break;
            case STACKDEPTH:
                // Pushes the stack depth onto the INTEGER stack.
                md.integers().push(floats.size());
                break;
            case TAN:
                // Pushes the tangent of the top item.
                if (floats.size() > 0) {
                    floats.push((float) Math.tan(floats.pop()));
                }
                break;
            case EXP:
                // Euler's number as base.
                if (floats.size() > 0) {
                    floats.push((float) Math.exp(floats.pop()));
                }
                break;
            case LOG:
                // The natural logarithm ('ln').
                if (floats.size() > 0) {
                    floats.push((float) Math.log(floats.pop()));
                }
                break;
            case SIGMOID:
                if (floats.size() > 0) {
                    floats.push(sigmoid(floats.pop()));
                }
                break;
            case ABS:
                if (floats.size() > 0) {
                    floats.push(abs(floats.pop()));
                }
                break;
            case GAUSSIAN:
                if (floats.size() > 0) {
                    floats.push((float) gaussianZeroMeanUnitStdDev.value(floats.pop()));
                }
                break;
            case RELU:
                if (floats.size() > 0) {
                    floats.push(max(0f, floats.pop()));
                }
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(literal));
        }
    }
}
