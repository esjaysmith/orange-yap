package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

import java.util.*;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 18:26
 */
class SequenceImpl implements Sequence {

    private final SequenceImpl parent;
    private final LinkedList<Program> elements;

    public SequenceImpl() {
        this((SequenceImpl) null);
    }

    public SequenceImpl(SequenceImpl parent) {
        this.parent = parent;
        this.elements = new LinkedList<>();
    }

    public SequenceImpl(Collection<Program> elements) {
        this.parent = null;
        this.elements = new LinkedList<>(elements);
    }

    SequenceImpl getParent() {
        return parent;
    }

    void addElement(Program p) {
        elements.add(p);
    }

    void addElement(int idx, Program p) {
        elements.add(idx, p);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    int indexOf(Object obj) {
        return elements.indexOf(obj);
    }

    void remove(int idx) {
        elements.remove(idx);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public List<Program> getElements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Sequence)) {
            return false;
        }
        Sequence other = (Sequence) obj;
        return Objects.equals(elements, other.getElements());
    }

    @Override
    public String toString() {
        return String.valueOf(elements);
    }
}
