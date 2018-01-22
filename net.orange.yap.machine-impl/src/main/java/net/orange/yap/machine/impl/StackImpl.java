package net.orange.yap.machine.impl;

import net.orange.yap.machine.stack.Stack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.Math.max;

/**
 * User: sjsmit
 * Date: 19/07/15
 * Time: 11:57
 */
class StackImpl<E> implements Stack<E> {

    protected final LinkedList<E> stack;
    private final int maxSize;

    StackImpl(int maxSize) {
        this.maxSize = maxSize;
        this.stack = new LinkedList<>();
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public Object[] toArray() {
        return stack.toArray();
    }

    @Override
    public boolean push(E b) {
        if (stack.size() < maxSize) {
            stack.addFirst(b);
            return true;
        }
        return false;
    }

    @Override
    public boolean push(int index, E e) {
        if (stack.size() < maxSize) {
            stack.add(index, e);
            return true;
        }
        return false;
    }

    @Override
    public E pop() {
        return stack.pop();
    }

    @Override
    public E pop(int index) {
        return stack.remove(max(0, index) % size());
    }

    @Override
    public E peek() {
        return stack.peek();
    }

    @Override
    public E peek(int index) {
        return stack.get(max(0, index) % size());
    }

    @Override
    public void clear() {
        stack.clear();
    }

    @Override
    public List<E> toList() {
        return Collections.unmodifiableList(stack);
    }

    @Override
    public Stream<E> stream() {
        return stack.stream();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StackImpl)) {
            return false;
        }
        StackImpl other = (StackImpl) obj;
        return Objects.equals(stack, other.stack);
    }

    @Override
    public String toString() {
        return "size " + stack.size();
    }
}
