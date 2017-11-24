package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Combine the two programs as flat lists of instructions.
 * <p>
 * User: sjsmit
 * Date: 05/02/16
 * Time: 22:59
 */
public class UltraCrossoverImpl implements CrossOverStrategy {

    private final Random random;

    public UltraCrossoverImpl(Random random) {
        this.random = random;
    }

    private static List<Instruction> asInstructions(Program p) {
        final List<Instruction> instructions = new ArrayList<>();
        Traversal.Callback callback = new Traversal.Callback() {
            @Override
            public void sequenceStart(Sequence parent, Sequence child) {
                instructions.add(Parenthesis.OPEN);
            }

            @Override
            public void sequenceEnd(Sequence parent, Sequence child) {
                instructions.add(Parenthesis.CLOSE);
            }

            @Override
            public void instruction(Sequence parent, Instruction child) {
                instructions.add(child);
            }
        };
        Traversal.traverse(p, callback);
        return instructions;
    }

    @Override
    public Program perform(Program program, Program with) {
        // Convert the programs into lists of instructions.
        final List<Instruction> left = asInstructions(program);
        final List<Instruction> right = asInstructions(with);
        final int shortest = Math.min(left.size(), right.size());

        final List<Instruction> instructions = new ArrayList<>();

        // Switch source list during processing until the end is reached.
        List<Instruction> source = random.nextBoolean() ? left : right;
        int index = 0;
        for (; index < shortest; index++) {
            instructions.add(source.get(index));
            source = random.nextBoolean() ? left : right;
        }
        // Do not always let the shorter program determine the result length.
        for (; index < source.size(); index++) {
            instructions.add(source.get(index));
        }

        // Translate the instructions to a program.
        SequenceImpl root = new SequenceImpl();
        SequenceImpl current = root;
        for (Instruction instruction : instructions) {
            if (instruction == Parenthesis.OPEN) {
                SequenceImpl child = new SequenceImpl(current);
                current.addElement(child);
                current = child;
            } else if (instruction == Parenthesis.CLOSE) {
                current = current.getParent() == null ? root : current.getParent();
            } else {
                current.addElement(instruction);
            }
        }
        return root;
    }

    private enum Parenthesis implements Instruction {
        OPEN, CLOSE
    }
}
