package net.orange.yap.machine;

import net.orange.yap.machine.code.CodeGenerator;
import net.orange.yap.machine.impl.YapRuntimeFactoryImpl;
import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 23/11/2017
 * Time: 11:56
 */
public class TestDeterminism extends AbstractRuntimeTests {

    private static final long seed = 0L;
    private static final int num_test = 1000;

    public void testGenerate() throws Exception {
        final CodeGenerator generator1 = new YapRuntimeFactoryImpl(seed).create().generator();
        final CodeGenerator generator2 = new YapRuntimeFactoryImpl(seed).create().generator();

        // Assert that newly created programs are repeatable.
        for (int i = 0; i < num_test; i++) {
            Program p1 = generator1.generate();
            Program p2 = generator2.generate();
            assertEquals(p1, p2);
            if (i % 50 == 0) {
                System.out.println(p1);
            }
        }
    }

    public void testCrossover() throws Exception {
        final CodeGenerator generator1 = new YapRuntimeFactoryImpl(seed).create().generator();
        final CodeGenerator generator2 = new YapRuntimeFactoryImpl(seed).create().generator();

        // Assert that crossover is repeatable.
        // Use a third generator to keep the amount of internal calls to random the same for both generators during crossover.
        final CodeGenerator code = new YapRuntimeFactoryImpl(seed).create().generator();
        for (int i = 0; i < num_test; i++) {
            Program p1 = code.generate();
            Program p2 = code.generate();
            Program c1 = generator1.combine(p1, p2);
            Program c2 = generator2.combine(p1, p2);
            assertEquals(c1, c2);
            if (i % 50 == 0) {
                System.out.println(c1);
            }
        }
    }
}
