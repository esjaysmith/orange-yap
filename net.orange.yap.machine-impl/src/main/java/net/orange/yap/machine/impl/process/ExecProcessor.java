package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Region;
import net.orange.yap.machine.stack.Stack;
import net.orange.yap.machine.stack.instruction.ExecInstruction;
import net.orange.yap.machine.stack.instruction.IntegerInstruction;
import net.orange.yap.machine.stack.literal.IntegerLiteral;

/**
 * User: sjsmit
 * Date: 26/07/15
 * Time: 11:59
 */
class ExecProcessor extends AbstractProcessor<Program> implements Processor<Instruction> {

    public ExecProcessor(Interpreter interpreter) {
        super(interpreter);
    }

    private static void handleDoRange(MachineInternal md, DoRangeInstruction dri) {
        int current = dri.current;
        int destination = dri.destination;
        Program p = dri.program;

        md.integers().push(current);
        if (current == destination) {
            md.exec().push(p);
        } else {
            int diff = destination > current ? 1 : -1;
            md.exec().push(new DoRangeInstruction(current + diff, destination, p));
            md.exec().push(p);
        }
    }

    @Override
    public void process(MachineInternal m, Instruction instruction) {
        if (instruction instanceof ExecInstruction) {
            processInternal(m, (ExecInstruction) instruction);
        } else if (instruction instanceof DoRangeInstruction) {
            DoRangeInstruction dri = (DoRangeInstruction) instruction;
            handleDoRange(m, dri);
        } else {
            throw new IllegalArgumentException("Cannot process " + instruction + ".");
        }
    }

    private void processInternal(MachineInternal md, ExecInstruction literal) {
        final Stack<Program> exec = md.exec();
        switch (literal) {
            case EQUALS:
                super.equals(md.booleans(), exec);
                break;
            case DUP:
                super.duplicate(exec);
                break;
            case FLUSH:
                exec.clear();
                break;
            case POP:
                super.pop(exec);
                break;
            case ROT:
                super.rotate(exec);
                break;
            case SHOVE:
                super.shove(md.integers(), exec);
                break;
            case SWAP:
                super.swap(exec);
                break;
            case YANK:
                super.yank(md.integers(), exec);
                break;
            case YANKDUP:
                super.yankdup(md.integers(), exec);
                break;
            case DEFINE:
                // Defines the name on top of the NAME stack as an instruction that will
                // push the top item of the EXEC stack back onto the EXEC stack.
                if (exec.size() > 0 && md.names().size() > 0) {
                    md.bindings().define(md.names().pop(), exec.pop());
                }
                break;
            case DO_COUNT:
                // An iteration instruction that performs a loop (the body of which is taken
                // from the EXEC stack) the number of times indicated by the INTEGER argument, pushing an
                // index (which runs from zero to one less than the number of iterations) onto the INTEGER
                // stack prior to each execution of the loop body. This is similar to CODE.DO*COUNT except
                // that it takes its code argument from the EXEC stack. This should be implemented as a macro
                // that expands into a call to EXEC.DO*RANGE. EXEC.DO*COUNT takes a single INTEGER argument
                // (the number of times that the loop will be executed) and a single EXEC argument
                // (the body of the loop). If the provided INTEGER argument is negative or zero then this
                // becomes a NOOP. Otherwise it expands into:
                // ( 0 <1 - IntegerArg> EXEC.DO*RANGE <ExecArg> )
                if (md.integers().size() > 0 && exec.size() > 0 && md.integers().peek() > 0) {
                    int count = md.integers().pop();
                    Program p = exec.pop();
                    exec.push(new ElementList(
                            new IntegerLiteral(0),
                            new IntegerLiteral(count - 1),
                            ExecInstruction.DO_RANGE,
                            p
                    ));
                }
                break;
            case DO_RANGE:
                // An iteration instruction that executes the top item on the EXEC stack a
                // number of times that depends on the top two integers, while also pushing the loop counter
                // onto the INTEGER stack for possible access during the execution of the body of the loop.
                // This is similar to CODE.DO*COUNT except that it takes its code argument from the EXEC stack.
                // The top integer is the "destination index" and the second integer is the "current index."
                //
                // First the code and the integer arguments are saved locally and popped. Then the integers
                // are compared. If the integers are equal then the current index is pushed onto the INTEGER
                // stack and the code (which is the "body" of the loop) is pushed onto the EXEC stack for
                // subsequent execution. If the integers are not equal then the current index will still be
                // pushed onto the INTEGER stack but two items will be pushed onto the EXEC stack
                // -- first a recursive call to EXEC.DO*RANGE (with the same code and destination index,
                // but with a current index that has been either incremented or decremented by 1 to be closer
                // to the destination index) and then the body code. Note that the range is inclusive of both
                // endpoints; a call with integer arguments 3 and 5 will cause its body to be executed 3 times,
                // with the loop counter having the values 3, 4, and 5. Note also that one can specify a loop
                // that "counts down" by providing a destination index that is less than the specified current index.
                if (exec.size() > 0 && md.integers().size() > 1) {
                    final int destination = md.integers().pop();
                    final int current = md.integers().pop();
                    handleDoRange(md, new DoRangeInstruction(destination, current, exec.pop()));
                }
                break;
            case DO_TIMES:
                // Like EXEC.DO*COUNT but does not push the loop counter. This should be
                // implemented as a macro that expands into EXEC.DO*RANGE, similarly to the implementation
                // of EXEC.DO*COUNT, except that a call to INTEGER.POP should be tacked on to the front of
                // the loop body code in the call to EXEC.DO*RANGE. This call to INTEGER.POP will remove the
                // loop counter, which will have been pushed by EXEC.DO*RANGE, prior to the execution of the loop body.
                if (md.integers().size() > 0 && exec.size() > 0 && md.integers().peek() > 0) {
                    int count = md.integers().pop();
                    Program p = exec.pop();
                    exec.push(new ElementList(
                            new IntegerLiteral(0),
                            new IntegerLiteral(count - 1),
                            ExecInstruction.DO_RANGE,
                            new ElementList(IntegerInstruction.POP, p)
                    ));
                }
                break;
            case IF:
                // If the top item of the BOOLEAN stack is TRUE then this removes the second item on the EXEC stack,
                // leaving the first item to be executed. If it is false then it removes the first item, leaving the
                // second to be executed. This is similar to CODE.IF except that it operates on the EXEC stack. This
                // acts as a NOOP unless there are at least two items on the EXEC stack and one item on the BOOLEAN
                // stack.
                if (md.booleans().size() > 0 && exec.size() > 1) {
                    exec.pop(md.booleans().pop() ? 1 : 0);
                }
                break;
            case K:
                // The Push implementation of the "K combinator". Removes the second item on the EXEC stack.
                if (exec.size() > 1) {
                    exec.pop(1);
                }
                break;
            case S:
                // The Push implementation of the "S combinator". Pops 3 items from the EXEC stack,
                // which we will call A, B, and C (with A being the first one popped). Then pushes a list
                // containing B and C back onto the EXEC stack, followed by another instance of C, followed
                // by another instance of A.
                if (exec.size() > 2) {
                    Program a = exec.pop(), b = exec.pop(), c = exec.pop();
                    exec.push(new ElementList(b, c));
                    exec.push(c);
                    exec.push(a);
                }
                break;
            case STACKDEPTH:
                // Pushes the stack depth onto the INTEGER stack.
                md.integers().push(exec.size());
                break;
            case Y:
                // The Push implementation of the "Y combinator". Inserts beneath the top item of the
                // EXEC stack a new item of the form "( EXEC.Y <TopItem> )".
                if (exec.size() > 0) {
                    exec.push(1, new ElementList(ExecInstruction.Y, exec.peek()));
                }
                break;
            case INITRNG:
                md.initRandomNumberGenerator();
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(literal));
        }
    }

    private static class DoRangeInstruction implements Instruction {

        private final int current, destination;
        private final Program program;

        public DoRangeInstruction(int current, int destination, Program program) {
            this.current = current;
            this.destination = destination;
            this.program = program;
        }

        @Override
        public Region getRegion() {
            return ExecInstruction.DO_RANGE.getRegion();
        }

        @Override
        public String getSymbol() {
            return ExecInstruction.DO_RANGE.getSymbol();
        }

        @Override
        public String toString() {
            return "_" + getRegion() + "." + getSymbol() + "_";
        }
    }
}
