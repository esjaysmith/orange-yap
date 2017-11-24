package net.orange.yap.machine.impl;

import net.orange.yap.machine.code.LiteralGenerator;
import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.literal.BooleanLiteral;
import net.orange.yap.machine.stack.literal.FloatLiteral;
import net.orange.yap.machine.stack.literal.IntegerLiteral;
import net.orange.yap.machine.stack.literal.NameLiteral;

import java.util.Random;

/**
 * User: sjsmit
 * Date: 23/11/2017
 * Time: 11:41
 */
class LiteralGeneratorImpl implements LiteralGenerator {
    private final Random random;
    private final int integerBound;

    LiteralGeneratorImpl(Random rnd, int integerBound) {
        this.random = rnd;
        this.integerBound = integerBound;
    }

    @Override
    public Instruction generateLiteral() {
        if (random.nextBoolean()) {
            return random.nextBoolean() ?
                    new BooleanLiteral(random.nextBoolean()) :
                    new FloatLiteral(random.nextFloat() * (random.nextBoolean() ? 1f : -1f));
        }
        return random.nextBoolean() ?
                new IntegerLiteral(random.nextInt(integerBound) * (random.nextBoolean() ? 1 : -1)) :
                new NameLiteral(generateName());

    }

    private String generateName() {
        return "lit-" + random.nextInt((int) 1e9);
    }
}
