package net.orange.yap.machine.impl.rand;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;

/**
 * User: sjsmit
 * Date: 12/08/15
 * Time: 13:21
 */
class Traversal {

    public static void traverse(Program program, Callback callback) {
        traverse(program, null, callback);
    }

    public static void traverse(Program node, Sequence parent, Callback callback) {
        if (node instanceof Sequence) {
            Sequence sequence = (Sequence) node;
            if (parent != null) {
                callback.sequenceStart(parent, sequence);
            }
            for (Program c : sequence.getElements()) {
                traverse(c, sequence, callback);
            }
            if (parent != null) {
                callback.sequenceEnd(parent, sequence);
            }
        } else {
            callback.instruction(parent, (Instruction) node);
        }
    }

    public interface Callback {

        default void sequenceStart(Sequence parent, Sequence child) {
        }

        default void sequenceEnd(Sequence parent, Sequence child) {
        }

        default void instruction(Sequence parent, Instruction child) {
        }
    }
}
