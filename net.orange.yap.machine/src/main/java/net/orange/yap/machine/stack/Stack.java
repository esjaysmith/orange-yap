package net.orange.yap.machine.stack;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * User: sjsmit
 * Date: 19/07/15
 * Time: 11:47
 */
public interface Stack<E> {

    int size();

    Object[] toArray();

    /**
     * Inserts the specified element at the front of this Stack if it is
     * possible to do so immediately without violating capacity restrictions.
     *
     * @param e the element to add
     * @throws IllegalStateException    if the element cannot be added at this
     *                                  time due to capacity restrictions
     * @throws ClassCastException       if the class of the specified element
     *                                  prevents it from being added to this stack
     * @throws NullPointerException     if the specified element is null and this
     *                                  stack does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     *                                  element prevents it from being added to this stack
     */
    boolean push(E e);

    boolean push(int index, E e);

    /**
     * Pops an element from the stack represented by this stack.  In other
     * words, removes and returns the first element of this stack.
     *
     * @return the element at the front of this stack (which is the top
     * of the stack represented by this stack)
     * @throws NoSuchElementException if this stack is empty
     */
    E pop();

    E pop(int index);

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this stack (in other words, the first element of this stack), or
     * returns <tt>null</tt> if this stack is empty.
     *
     * @return the head of the queue represented by this stack, or
     * <tt>null</tt> if this stack is empty
     */
    E peek();

    E peek(int index);

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this collection
     */
    void clear();

    List<E> toUnmodifiableList();

    Stream<E> stream();
}
