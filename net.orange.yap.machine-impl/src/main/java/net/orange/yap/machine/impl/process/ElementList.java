package net.orange.yap.machine.impl.process;

import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

import java.util.*;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 23:02
 */
class ElementList implements Sequence {

    private final List<Program> elements;

    public ElementList(Program... elements) {
        this(elements == null ? Collections.<Program>emptyList() : Arrays.asList(elements));
    }

    public ElementList(Collection<Program> elements) {
        this.elements = new ArrayList<>(elements);
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
