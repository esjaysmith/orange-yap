package net.orange.yap.machine.impl.code;

import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 18:26
 */
class SequenceImpl implements Sequence {

    private final SequenceImpl parent;
    private final LinkedList<Program> elements;

    public SequenceImpl() {
        this.parent = this;
        this.elements = new LinkedList<>();
    }

    public SequenceImpl(SequenceImpl parent) {
        this.parent = parent;
        this.elements = new LinkedList<>();
    }

    void addElement(Program p) {
        elements.add(p);
    }

    SequenceImpl getParent() {
        return parent;
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
        if (!(obj instanceof SequenceImpl)) {
            return false;
        }
        SequenceImpl other = (SequenceImpl) obj;
        // Direct pointer comparison for parent.
        return parent == other.parent
                && Objects.equals(elements, other.elements);
    }

    @Override
    public String toString() {
        return String.valueOf(elements);
    }
}
