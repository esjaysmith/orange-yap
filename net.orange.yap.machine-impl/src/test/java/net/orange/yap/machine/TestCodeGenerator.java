package net.orange.yap.machine;

import net.orange.yap.machine.impl.ExecutionContextImpl;
import net.orange.yap.machine.stack.Program;

import java.util.Random;

import static net.orange.yap.machine.util.MachineUtils.isEmptySequence;

/**
 * User: sjsmit
 * Date: 06/08/15
 * Time: 22:13
 */
public class TestCodeGenerator extends AbstractRuntimeTests {

    private static final Random random = new Random();

    public void testPrintRandomCode() throws Exception {
        for (int i = 0; i < 20; i++) {
            System.out.println(rt.asString(rt.generator().generate()));
        }
    }

    public void testExecRandomCode() throws Exception {
        int counter = 0;
        long totalExecutedInstructions = 0;
        while (++counter < 1e4) {
            final ExecutionContextImpl context = new ExecutionContextImpl(random.nextInt(100));
            Machine m = rt.createMachineBuilder()
                    .setContext(context)
                    .setProgram(rt.generator().generate())
                    .build();
            m.booleans().push(true);
            m.booleans().push(true);
            m.booleans().push(false);
            m.booleans().push(false);
            rt.execute(m);
            totalExecutedInstructions += context.getInstructions();

            if (counter % 1e3 == 0) {
                System.out.println(counter + ":\t" + String.valueOf(m.booleans()));
                System.out.println(counter + ":\t" + String.valueOf(m.floats()));
                System.out.println(counter + ":\t" + String.valueOf(m.integers()));
                System.out.println(counter + ":\t" + String.valueOf(m.listCodeItems()));
                System.out.println(counter + ":\t" + String.valueOf(m.listExecutionItems()));
                System.out.println(counter + ":\t#instructions = " + context.getInstructions());
                System.out.println();
            }
        }
        System.out.printf("Total #instructions executed = " + totalExecutedInstructions + ".");
    }

    public void testCrossover() throws Exception {
        for (int h = 0; h < 5; h++) {
            Program left = rt.generator().generate();
            Program right = rt.generator().generate();

            for (int i = 0; i < 10; i++) {
                System.out.println(i + "\t" + left + " x " + right);
                Program one = rt.generator().combine(left, right);
                right = rt.generator().combine(right, left);
                left = one;
            }

            System.out.println();
        }
    }

    public void testMutation() throws Exception {
        for (int h = 0; h < 5; h++) {
            Program p = rt.generator().generate();
            System.out.println(p);
            for (int i = 0; i < 50 && !isEmptySequence(p); i++) {
                System.out.println(i + "\t" + rt.generator().mutate(p));
            }

            System.out.println();
        }
    }
}
