package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.code.LiteralGenerator;
import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Program;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 11:32
 */
public class InstructionGeneratorImpl implements InstructionGenerator {

    private final Random random;
    private final float percentageOfLiterals;
    private final LiteralGenerator literalGenerator;
    private final List<Instruction> instructions;


    public InstructionGeneratorImpl(Random random, float literals, LiteralGenerator generator, List<Instruction> instructions) {
        this.random = random;
        this.percentageOfLiterals = literals;
        this.literalGenerator = generator;
        this.instructions = instructions;
    }

    @Override
    public Instruction generate() {
        if (random.nextFloat() < percentageOfLiterals) {
            return literalGenerator.generateLiteral();
        }

        return instructions.get(random.nextInt(instructions.size()));
    }

    @Override
    public Program generate(int points) {
        if (points < 1) {
            throw new IllegalArgumentException("Points must be a positive number.");
        }

        if (points == 1) {
            return generate();
        }

        SequenceImpl sequence = new SequenceImpl();
        generateWithSize(sequence, points);
        return sequence;
    }

    private void generateWithSize(SequenceImpl sequence, int points) {
        if (points == 1) {
            sequence.addElement(generate());
            return;
        }

        decompose(sequence, points - 1, points - 1);
    }

    private void decompose(SequenceImpl sequence, int number, int maximumParts) {
        if (number == 1 || maximumParts == 1) {
            generateWithSize(sequence, 1);
            return;
        }

        // Set THIS-PART to be a random number between 1 and (NUMBER - 1).
        final int thisPart = number == 2 ? 1 : random.nextInt(number - 2) + 1;
        final Collection<Program> instructions = generateInstructions(thisPart);
        sequence.addElement(instructions.size() == 1 ?
                instructions.iterator().next() :
                new SequenceImpl(instructions));
        decompose(sequence, number - thisPart, maximumParts - 1);
    }

    private Collection<Program> generateInstructions(int parts) {
        List<Program> list = new ArrayList<>(parts);
        int i = 0;
        while (i++ < parts) {
            list.add(generate());
        }

        return list;
    }

}
