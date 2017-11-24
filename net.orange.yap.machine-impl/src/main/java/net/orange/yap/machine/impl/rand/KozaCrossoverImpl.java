package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

import java.util.*;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 11:45
 */
public class KozaCrossoverImpl implements CrossOverStrategy {

    private final Random random;

    public KozaCrossoverImpl(Random random) {
        this.random = random;
    }

    @Override
    public Program perform(Program program, Program with) {
        // Traverse the program and copy it. At the same time create a list of its elements.
        final List<Element> pElements = new ArrayList<>();
        // The top level sequence of the copy.
        final SequenceImpl copyRoot = new SequenceImpl();
        // Pointers of original sequences to copies.
        final Map<Sequence, SequenceImpl> map = new HashMap<>();
        if (program instanceof Sequence) {
            map.put((Sequence) program, copyRoot);
        }

        Traversal.Callback callback = new Traversal.Callback() {
            @Override
            public void sequenceStart(Sequence parent, Sequence child) {
                // Add a new sequence and update the pointers.
                SequenceImpl parentCopy = map.get(parent);
                if (parentCopy == null) {
                    parentCopy = new SequenceImpl();
                    map.put(parent, parentCopy);
                }
                // Any child sequence can only be met once.
                SequenceImpl childCopy = new SequenceImpl();
                map.put(child, childCopy);
                parentCopy.addElement(childCopy);
                pElements.add(new Element(parentCopy, childCopy));
            }

            @Override
            public void instruction(Sequence parent, Instruction child) {
                SequenceImpl parentCopy = parent == null ? copyRoot : map.get(parent);
                parentCopy.addElement(child);
                pElements.add(new Element(parentCopy, child));
            }
        };
        Traversal.traverse(program, null, callback);

        // The list of elements of the other program.
        final List<Element> wElements = new ArrayList<>();
        Traversal.traverse(with, null, new Traversal.Callback() {
            @Override
            public void sequenceStart(Sequence parent, Sequence child) {
                wElements.add(new Element(parent, child));
            }

            @Override
            public void instruction(Sequence parent, Instruction child) {
                wElements.add(new Element(parent, child));
            }
        });

        // Use the unmodified original when either is empty.
        if (pElements.isEmpty() || wElements.isEmpty()) {
            return program;
        }

        // Select a node to be replaced.
        Element replace = pElements.get(random.nextInt(pElements.size()));
        Element next = wElements.get(random.nextInt(wElements.size()));

        SequenceImpl sequenceImpl = replace.parent == null ? copyRoot : (SequenceImpl) replace.parent;
        int idx = sequenceImpl.indexOf(replace.node);
        sequenceImpl.remove(idx);
        sequenceImpl.addElement(idx, next.node);

        return copyRoot;
    }

    private static class Element {
        private final Sequence parent;
        private final Program node;

        public Element(Sequence parent, Program node) {
            this.parent = parent;
            this.node = node;
        }
    }
}
