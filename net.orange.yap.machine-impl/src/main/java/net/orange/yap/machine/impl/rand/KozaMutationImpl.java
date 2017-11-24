package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 13:18
 */
public class KozaMutationImpl implements MutationStrategy {

    private final Random random;
    private final float chance;
    private final InstructionGenerator instructions;
    private final int minimumTrials = 5;
    private final int maximumTrials = 10;

    public KozaMutationImpl(Random random, float chance, InstructionGenerator instructions) {
        this.random = random;
        this.chance = chance;
        this.instructions = instructions;
    }

    @Override
    public Program mutate(final Program program) {
        // Traverse the program and copy it.
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

                if (random.nextFloat() < chance) {
                    if (random.nextBoolean()) {
                        parentCopy.addElement(generate());
                    }
                } else {
                    parentCopy.addElement(childCopy);
                }
            }

            @Override
            public void instruction(Sequence parent, Instruction child) {
                SequenceImpl parentCopy = parent == null ? copyRoot : map.get(parent);
                if (random.nextFloat() < chance) {
                    if (random.nextBoolean()) {
                        parentCopy.addElement(generate());
                    }
                } else {
                    parentCopy.addElement(child);
                }
            }
        };
        Traversal.traverse(program, callback);

        return copyRoot;
    }

    public Program generate() {
        return instructions.generate((int) (Math.pow(2d, trials() - 1)));
    }

    /**
     * Elements from {0,1} are drawn with equal probability until the first '1' is drawn.
     * Let t denote the number of trials.
     *
     * @return t.
     */
    private int trials() {
        int trials = minimumTrials;
        boolean drawn = false;
        while (!drawn && trials < maximumTrials) {
            drawn = random.nextBoolean();
            trials++;
        }
        return trials;
    }
}
