package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.*;
import net.orange.yap.machine.stack.instruction.CodeInstruction;
import net.orange.yap.machine.stack.instruction.IntegerInstruction;
import net.orange.yap.machine.stack.literal.BooleanLiteral;
import net.orange.yap.machine.stack.literal.FloatLiteral;
import net.orange.yap.machine.stack.literal.IntegerLiteral;
import net.orange.yap.machine.stack.literal.NameLiteral;
import net.orange.yap.machine.util.MachineUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static net.orange.yap.machine.util.MachineUtils.isEmptySequence;

/**
 * User: sjsmit
 * Date: 26/07/15
 * Time: 11:59
 */
class CodeProcessor extends AbstractProcessor<Program> implements Processor<Instruction> {

    private final int maxCodeElements;

    CodeProcessor(Interpreter interpreter, int maxCodeElements) {
        super(interpreter);
        this.maxCodeElements = maxCodeElements;
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

    private static Collection<Program> getElements(Program p) {
        return p instanceof Instruction ? Collections.singleton(p) : ((Sequence) p).getElements();
    }

    @Override
    public void process(MachineInternal m, Instruction instruction) {
        if (instruction instanceof CodeInstruction) {
            processInternal(m, (CodeInstruction) instruction);
        } else if (instruction instanceof DoRangeInstruction) {
            DoRangeInstruction dri = (DoRangeInstruction) instruction;
            handleDoRange(m, dri);
        } else {
            throw new IllegalArgumentException("Cannot process " + instruction + ".");
        }
    }

    private void processInternal(MachineInternal md, CodeInstruction literal) {
        final Stack<Program> code = md.code();
        switch (literal) {
            case EQUALS:
                super.equals(md.booleans(), code);
                break;
            case DUP:
                super.duplicate(code);
                break;
            case FLUSH:
                code.clear();
                break;
            case POP:
                super.pop(code);
                break;
            case ROT:
                super.rotate(code);
                break;
            case SHOVE:
                super.shove(md.integers(), code);
                break;
            case SWAP:
                super.swap(code);
                break;
            case YANK:
                super.yank(md.integers(), code);
                break;
            case YANKDUP:
                super.yankdup(md.integers(), code);
                break;
            case APPEND:
                // Pushes the result of appending the top two pieces of code. If one of the pieces of code is a
                // single instruction or literal (that is, something not surrounded by parentheses) then it is
                // surrounded by parentheses first.
                if (code.size() > 1) {
                    List<Program> list = new ArrayList<>();
                    list.addAll(getElements(code.peek()));
                    list.addAll(getElements(code.peek(1)));
                    if (list.size() > maxCodeElements) {
                        break;
                    }
                    code.pop();
                    code.pop();
                    code.push(new ElementList(list));
                }
                break;
            case ATOM:
                // Pushes TRUE onto the BOOLEAN stack if the top piece of code is a single instruction or a
                // literal, and FALSE otherwise (that is, if it is something surrounded by parentheses).
                if (code.size() > 0) {
                    final Program p = code.pop();
                    md.booleans().push(p instanceof Instruction);
                }
                break;
            case CAR:
                // Pushes the first item of the list on top of the CODE stack. For example, if the top piece
                // of code is "( A B )" then this pushes "A" (after popping the argument). If the code on top
                // of the stack is not a list then this has no effect. The name derives from the similar Lisp
                // function; a more generic name would be "FIRST".
                if (code.size() > 0 && code.peek() instanceof Sequence) {
                    Sequence sequence = (Sequence) code.pop();
                    if (sequence.size() > 0) {
                        code.push(sequence.getElements().get(0));
                    }
                }
                break;
            case CDR:
                // Pushes a version of the list from the top of the CODE stack without its first element.
                // For example, if the top piece of code is "( A B )" then this pushes "( B )" (after popping
                // the argument). If the code on top of the stack is not a list then this pushes the empty list
                // ("( )"). The name derives from the similar Lisp function; a more generic name would be "REST".
                if (code.size() > 0) {
                    Program p = code.pop();
                    if (p instanceof Sequence) {
                        Sequence sequence = (Sequence) p;
                        Collection<Program> cdr = sequence.size() < 2 ?
                                Collections.emptyList() :
                                sequence.getElements().subList(1, sequence.size());
                        code.push(new ElementList(cdr));
                        break;
                    }

                    code.push(new ElementList());
                }
                break;
            case CONS:
                // Pushes the result of "consing" (in the Lisp sense) the second stack item onto the first stack
                // item (which is coerced to a list if necessary). For example, if the top piece of code is
                // "( A B )" and the second piece of code is "X" then this pushes "( X A B )" (after popping
                // the argument).
                if (code.size() > 1) {
                    final Collection<Program> first = getElements(code.peek());
                    final Collection<Program> second = getElements(code.peek(1));
                    List<Program> list = new ArrayList<>();
                    list.addAll(second);
                    list.addAll(first);
                    if (list.size() > maxCodeElements) {
                        break;
                    }
                    code.pop();
                    code.pop();
                    code.push(new ElementList(list));
                }
                break;
            //case CONTAINER:
            // Pushes the "container" of the second CODE stack item within the first CODE stack item onto
            // the CODE stack. If second item contains the first anywhere (i.e. in any nested list) then the
            // container is the smallest sub-list that contains but is not equal to the first instance.
            // For example, if the top piece of code is "( B ( C ( A ) ) ( D ( A ) ) )" and the second piece
            // of code is "( A )" then this pushes ( C ( A ) ). Pushes an empty list if there is no such
            // container.
            //  break;
            //case CONTAINS:
            // Pushes TRUE on the BOOLEAN stack if the second CODE stack item contains the first CODE stack
            // item anywhere (e.g. in a sub-list).
            //  break;
            case DEFINE:
                // Defines the name on top of the NAME stack as an instruction that will push the top item of the
                // CODE stack onto the EXEC stack.
                if (code.size() > 0 && md.names().size() > 0) {
                    md.bindings().define(md.names().pop(), code.pop());
                }
                break;
            case DEFINITION:
                // Pushes the definition associated with the top NAME on the NAME stack (if any) onto the CODE
                // stack. This extracts the definition for inspection/manipulation, rather than for immediate
                // execution (although it may then be executed with a call to CODE.DO or a similar instruction).
                if (md.names().size() > 0) {
                    Program p = md.bindings().get(md.names().pop());
                    if (p != null) {
                        code.push(p);
                    }
                }
                break;
            //case DISCREPANCY:
            // Pushes a measure of the discrepancy between the top two CODE stack items onto the INTEGER stack.
            // This will be zero if the top two items are equivalent, and will be higher the 'more different'
            // the items are from one another. The calculation is as follows:
            // 1. Construct a list of all of the unique items in both of the lists (where uniqueness is
            //      determined by equalp). Sub-lists and atoms all count as items.
            // 2. Initialize the result to zero.
            // 3. For each unique item increment the result by the difference between the number of occurrences
            //      of the item in the two pieces of code.
            // 4. Push the result.
            // break;
            case DO:
                // Recursively invokes the interpreter on the program on top of the CODE stack. After evaluation
                // the CODE stack is popped; normally this pops the program that was just executed, but if the
                // expression itself manipulates the stack then this final pop may end up popping something else.
                // Prevent stack-overflow when two code.do instructions are executed one after another.
                if (code.size() > 0 && !CodeInstruction.DO.equals(code.peek())) {
                    interpreter.execute(md, code.peek());
                    if (code.size() > 0) {
                        code.pop();
                    }
                }
                break;
            case DO_STAR:
                // Like CODE.DO but pops the stack before, rather than after, the recursive execution.
                if (code.size() > 0) {
                    interpreter.execute(md, code.pop());
                }
                break;
            case DO_COUNT:
                // An iteration instruction that performs a loop (the body of which is taken from the CODE stack)
                // the number of times indicated by the INTEGER argument, pushing an index (which runs from zero to
                // one less than the number of iterations) onto the INTEGER stack prior to each execution of the
                // loop body. This should be implemented as a macro that expands into a call to CODE.DO*RANGE.
                // CODE.DO*COUNT takes a single INTEGER argument (the number of times that the loop will be executed)
                // and a single CODE argument (the body of the loop). If the provided INTEGER argument is negative
                // or zero then this becomes a NOOP. Otherwise it expands into:
                // ( 0 <1 - IntegerArg> CODE.QUOTE <CodeArg> CODE.DO*RANGE )
                if (md.integers().size() > 0 && code.size() > 0 && md.integers().peek() > 0) {
                    int count = md.integers().pop();
                    Program p = code.pop();
                    code.push(new ElementList(
                            new IntegerLiteral(0),
                            new IntegerLiteral(count - 1),
                            CodeInstruction.DO_RANGE,
                            p
                    ));
                }
                break;
            case DO_RANGE:
                // An iteration instruction that executes the top item on the CODE stack a number of times that
                // depends on the top two integers, while also pushing the loop counter onto the INTEGER stack for
                // possible access during the execution of the body of the loop. The top integer is the
                // "destination index" and the second integer is the "current index." First the code and the
                // integer arguments are saved locally and popped. Then the integers are compared. If the integers
                // are equal then the current index is pushed onto the INTEGER stack and the code (which is the
                // "body" of the loop) is pushed onto the EXEC stack for subsequent execution. If the integers are
                // not equal then the current index will still be pushed onto the INTEGER stack but two items will
                // be pushed onto the EXEC stack -- first a recursive call to CODE.DO*RANGE (with the same code and
                // destination index, but with a current index that has been either incremented or decremented by 1
                // to be closer to the destination index) and then the body code. Note that the range is inclusive
                // of both endpoints; a call with integer arguments 3 and 5 will cause its body to be executed 3 times,
                // with the loop counter having the values 3, 4, and 5. Note also that one can specify a loop that
                // "counts down" by providing a destination index that is less than the specified current index.
                if (code.size() > 0 && md.integers().size() > 1) {
                    final int destination = md.integers().pop();
                    final int current = md.integers().pop();
                    handleDoRange(md, new DoRangeInstruction(destination, current, code.pop()));
                }
                break;
            case DO_TIMES:
                // Like CODE.DO*COUNT but does not push the loop counter. This should be implemented as a macro
                // that expands into CODE.DO*RANGE, similarly to the implementation of CODE.DO*COUNT, except that
                // a call to INTEGER.POP should be tacked on to the front of the loop body code in the call to
                // CODE.DO*RANGE. This call to INTEGER.POP will remove the loop counter, which will have been pushed
                // by CODE.DO*RANGE, prior to the execution of the loop body.
                if (md.integers().size() > 0 && code.size() > 0 && md.integers().peek() > 0) {
                    int count = md.integers().pop();
                    Program p = code.pop();
                    code.push(new ElementList(
                            new IntegerLiteral(0),
                            new IntegerLiteral(count - 1),
                            CodeInstruction.DO_RANGE,
                            new ElementList(IntegerInstruction.POP, p)
                    ));
                }
                break;
            //case EXTRACT:
            // Pushes the sub-expression of the top item of the CODE stack that is indexed by the top item of
            // the INTEGER stack. The indexing here counts "points," where each parenthesized expression and each
            // literal/instruction is considered a point, and it proceeds in depth first order. The entire piece
            // of code is at index 0; if it is a list then the first item in the list is at index 1, etc. The
            // integer used as the index is taken modulo the number of points in the overall expression
            // (and its absolute value is taken in case it is negative) to ensure that it is within the meaningful
            // range.
            //  break;
            case FROMBOOLEAN:
                // Pops the BOOLEAN stack and pushes the popped item (TRUE or FALSE) onto the CODE stack.
                if (md.booleans().size() > 0) {
                    code.push(new BooleanLiteral(md.booleans().pop()));
                }
                break;
            case FROMFLOAT:
                // Pops the FLOAT stack and pushes the popped item onto the CODE stack.
                if (md.floats().size() > 0) {
                    code.push(new FloatLiteral(md.floats().pop()));
                }
                break;
            case FROMINTEGER:
                // Pops the INTEGER stack and pushes the popped integer onto the CODE stack.
                if (md.integers().size() > 0) {
                    code.push(new IntegerLiteral(md.integers().pop()));
                }
                break;
            case FROMNAME:
                // Pops the NAME stack and pushes the popped item onto the CODE stack.
                if (md.names().size() > 0) {
                    code.push(new NameLiteral(md.names().pop()));
                }
                break;
            case IF:
                // If the top item of the BOOLEAN stack is TRUE this recursively executes the second item of the
                // CODE stack; otherwise it recursively executes the first item of the CODE stack. Either way both
                // elements of the CODE stack (and the BOOLEAN value upon which the decision was made) are popped.
                if (md.booleans().size() > 0 && code.size() > 1) {
                    Program first = code.pop(), second = code.pop();
                    md.exec().push(md.booleans().pop() ? second : first);
                }
                break;
            //case INSERT:
            // Pushes the result of inserting the second item of the CODE stack into the first item, at the
            // position indexed by the top item of the INTEGER stack (and replacing whatever was there formerly).
            // The indexing is computed as in CODE.EXTRACT.
            //  break;
            //case INSTRUCTIONS:
            // Pushes a list of all active instructions in the interpreter's current configuration.
            //  break;
            case LENGTH:
                // Pushes the length of the top item on the CODE stack onto the INTEGER stack. If the top item is
                // not a list then this pushes a 1. If the top item is a list then this pushes the number of items
                // in the top level of the list; that is, nested lists contribute only 1 to this count, no matter
                // what they contain.
                if (code.size() > 0) {
                    final Program p = code.pop();
                    md.integers().push(p instanceof Instruction ? 1 : ((Sequence) p).size());
                }
                break;
            case LIST:
                // Pushes a list of the top two items of the CODE stack onto the CODE stack.
                if (code.size() > 1) {
                    code.push(new ElementList(code.pop(), code.pop()));
                }
                break;
            //case MEMBER:
            // Pushes TRUE onto the BOOLEAN stack if the second item of the CODE stack is a member of the first
            // item (which is coerced to a list if necessary).
            //  break;
            case NOOP:
                // Does nothing.
                break;
            case NTH:
                // Pushes the nth element of the expression on top of the CODE stack (which is coerced to a list
                // first if necessary). If the expression is an empty list then the result is an empty list. N is
                // taken from the INTEGER stack and is taken modulo the length of the expression into which it is
                // indexing.
                if (code.size() > 0 && md.integers().size() > 0) {
                    final Program p = code.pop();
                    if (isEmptySequence(p)) {
                        code.push(p);
                        break;
                    }

                    final Sequence list = p instanceof Instruction ? new ElementList(p) : (Sequence) p;
                    final int index = Math.abs(md.integers().pop() % list.size());
                    code.push(list.getElements().get(index));
                }
                break;
            case NTHCDR:
                // Pushes the nth "CDR" (in the Lisp sense) of the expression on top of the CODE stack (which is
                // coerced to a list first if necessary). If the expression is an empty list then the result is an
                // empty list. N is taken from the INTEGER stack and is taken modulo the length of the expression
                // into which it is indexing. A "CDR" of a list is the list without its first element.
                if (code.size() > 0 && md.integers().size() > 0) {
                    final Program p = code.pop();
                    if (isEmptySequence(p)) {
                        code.push(p);
                        break;
                    }

                    final Sequence list = p instanceof Instruction ? new ElementList(p) : (Sequence) p;
                    final int idx = Math.abs(md.integers().pop() % list.size());
                    code.push(new ElementList(list.getElements().subList(idx, list.size())));
                }
                break;
            case NULL:
                // Pushes TRUE onto the BOOLEAN stack if the top item of the CODE stack is an empty list, or
                // FALSE otherwise.
                if (code.size() > 0) {
                    Program p = code.pop();
                    md.booleans().push(isEmptySequence(p));
                }
                break;
            //case POSITION:
            // Pushes onto the INTEGER stack the position of the second item on the CODE stack within the
            // first item (which is coerced to a list if necessary). Pushes -1 if no match is found.
            //  break;
            case QUOTE:
                // Specifies that the next expression submitted for execution will instead be pushed literally onto
                // the CODE stack. This can be implemented by moving the top item on the EXEC stack onto the CODE stack.
                if (md.exec().size() > 0) {
                    code.push(md.exec().pop());
                }
                break;
            //case RAND:
            // Pushes a newly-generated random program onto the CODE stack. The limit for the size of the
            // expression is taken from the INTEGER stack; to ensure that it is in the appropriate range this is
            // taken modulo the value of the MAX-POINTS-IN-RANDOM-EXPRESSIONS parameter and the absolute value of
            // the result is used.
            //  break;
            case SIZE:
                // Pushes the number of "points" in the top piece of CODE onto the INTEGER stack. Each instruction,
                // literal, and pair of parentheses counts as a point.
                if (code.size() > 0) {
                    md.integers().push(MachineUtils.countPoints(code.peek()));
                }
                break;
            case STACKDEPTH:
                // Pushes the stack depth onto the INTEGER stack.
                md.integers().push(code.size());
                break;
            //case SUBST:
            // Pushes the result of substituting the third item on the code stack for the second item in the
            // first item. As of this writing this is implemented only in the Lisp implementation, within which
            // it relies on the Lisp "subst" function. As such, there are several problematic possibilities;
            // for example "dotted-lists" can result in certain cases with empty-list arguments. If any of these
            // problematic possibilities occurs the stack is left unchanged.
            // break;
            default:
                throw new IllegalArgumentException(String.valueOf(literal));
        }
    }

    private static class DoRangeInstruction implements Instruction {

        private final int current, destination;
        private final Program program;

        DoRangeInstruction(int current, int destination, Program program) {
            this.current = current;
            this.destination = destination;
            this.program = program;
        }

        @Override
        public Region getRegion() {
            return CodeInstruction.DO_RANGE.getRegion();
        }

        @Override
        public String getSymbol() {
            return CodeInstruction.DO_RANGE.getSymbol();
        }

        @Override
        public String toString() {
            return "_" + getRegion() + "." + getSymbol() + "_";
        }
    }
}
