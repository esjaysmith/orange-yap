package net.orange.yap.machine.util;

import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 14:39
 */
public class MachineUtils {

    public static int countPoints(Program p) {
        int total = 0;
        if (p instanceof Sequence) {
            // Sequences themselves also count.
            total += 2;
            for (Program program : ((Sequence) p).getElements()) {
                total += countPoints(program);
            }
        } else {
            total++;
        }
        return total;
    }

    public static boolean isEmptySequence(Program p) {
        return p instanceof Sequence && ((Sequence) p).getElements().isEmpty();
    }
}
