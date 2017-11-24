package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.impl.Interpreter;
import net.orange.yap.machine.stack.Stack;

/**
 * User: sjsmit
 * Date: 26/07/15
 * Time: 19:32
 */
abstract class AbstractProcessor<T> {

    protected final Interpreter interpreter;

    public AbstractProcessor(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    protected void equals(Stack<Boolean> booleans, Stack<T> stack) {
        // Pushes TRUE if the top two items are equal, or FALSE otherwise.
        if (stack.size() > 1) {
            booleans.push(stack.pop().equals(stack.pop()));
        }
    }

    protected void duplicate(Stack<T> stack) {
        // Duplicates the top item on the stack. Does not pop its argument.
        if (stack.size() > 0) {
            stack.push(stack.peek());
        }
    }

    protected void pop(Stack<T> stack) {
        if (stack.size() > 0) {
            stack.pop();
        }
    }

    protected void rotate(Stack<T> stack) {
        // Rotates the top three items on the stack, pulling the third item out and pushing it on top.
        if (stack.size() > 2) {
            stack.push(stack.pop(2));
        }
    }

    protected void shove(Stack<Integer> integers, Stack<T> stack) {
        // Inserts the top item "deep" in the stack, at the position indexed by the top INTEGER.
        if (doStackManipulation(integers, stack)) {
            int index = integers.pop();
            int size = stack.size();
            stack.push(index <= 0 ? 0 : index >= size ? size - 1 : index, stack.pop());
        }
    }

    protected void swap(Stack<T> stack) {
        // Swaps the top two items.
        if (stack.size() > 1) {
            stack.push(stack.pop(1));
        }
    }

    protected void yank(Stack<Integer> integers, Stack<T> stack) {
        // Removes an indexed item from "deep" in the stack and pushes it on top of the stack.
        // The index is taken from the INTEGER stack.
        if (doStackManipulation(integers, stack)) {
            int index = integers.pop();
            int size = stack.size();
            stack.push(stack.pop(index <= 0 ? 0 : index >= size ? size - 1 : index));
        }
    }

    protected void yankdup(Stack<Integer> integers, Stack<T> stack) {
        // Pushes a copy of an indexed item "deep" in the stack onto the top of the stack, without
        // removing the deep item. The index is taken from the INTEGER stack.
        if (doStackManipulation(integers, stack)) {
            stack.push(stack.peek(integers.pop()));
        }
    }

    private boolean doStackManipulation(Stack<Integer> integers, Stack<T> stack) {
        return (stack.equals(integers) && integers.size() > 1)
                || (!stack.equals(integers) && stack.size() > 0 && integers.size() > 0);
    }
}

