package net.orange.yap.machine.stack.instruction;

import net.orange.yap.machine.stack.Instruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: sjsmit
 * Date: 06/08/15
 * Time: 22:18
 */
public enum InstructionSet {

    ALL(
            BooleanInstruction.values(),
            CodeInstruction.values(),
            ExecInstruction.values(),
            FloatInstruction.values(),
            IntegerInstruction.values(),
            NameInstruction.values()
    ),

    PRIMITIVES(
            BooleanInstruction.values(),
            FloatInstruction.values(),
            IntegerInstruction.values()
    );

    private final List<Instruction> values;

    InstructionSet(Instruction[]... items) {
        this(convert(items));
    }

    InstructionSet(List<Instruction> items) {
        this.values = Collections.unmodifiableList(items);
    }

    private static List<Instruction> convert(Instruction[][] items) {
        List<Instruction> list = new ArrayList<>();
        for (Instruction[] instructions : items) {
            list.addAll(Arrays.asList(instructions));
        }
        return list;
    }

    public List<Instruction> getValues() {
        return values;
    }
}
