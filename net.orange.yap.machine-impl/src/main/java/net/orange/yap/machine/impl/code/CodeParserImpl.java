package net.orange.yap.machine.impl.code;

import net.orange.yap.machine.code.CodeParser;
import net.orange.yap.machine.code.CodeSerializer;
import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Literal;
import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;
import net.orange.yap.machine.stack.instruction.*;
import net.orange.yap.machine.stack.literal.BooleanLiteral;
import net.orange.yap.machine.stack.literal.FloatLiteral;
import net.orange.yap.machine.stack.literal.IntegerLiteral;
import net.orange.yap.machine.stack.literal.NameLiteral;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 17:56
 */
public class CodeParserImpl implements CodeParser, CodeSerializer {

    private final Map<String, Instruction> instructions;

    public CodeParserImpl() {
        this.instructions = new HashMap<>();
        initialize();
    }

    private static String key(Instruction instruction) {
        return instruction.getRegion().getSymbol() + "." + instruction.getSymbol();
    }

    /**
     * Any identifiers that do not represent known Push instructions or literals of other type
     * (e.g. TRUE and FALSE) are recognized as NAMEs.
     */
    private static NameLiteral createNameLiteral(String token) {
        if (token.indexOf('.') != -1) {
            throw new IllegalArgumentException(
                    "The expression appears to be an instruction because it contains a dot: ["
                            + token + "]."
            );
        }
        return new NameLiteral(token);
    }

    private static String asString(Instruction instruction) {
        return instruction instanceof Literal ? instruction.getSymbol() :
                instruction.getRegion().getSymbol() + "." + instruction.getSymbol();
    }

    private void initialize() {
        addInstructions(BooleanInstruction.values());
        addInstructions(CodeInstruction.values());
        addInstructions(ExecInstruction.values());
        addInstructions(FloatInstruction.values());
        addInstructions(IntegerInstruction.values());
        addInstructions(NameInstruction.values());
    }

    private void addInstructions(Instruction... values) {
        for (Instruction instruction : values) {
            instructions.put(key(instruction).toUpperCase(), instruction);
        }
    }

    @Override
    public Program fromString(String code) {
        final SequenceImpl program = new SequenceImpl();
        if (code != null) {
            code = code.trim();
            if (code.length() > 0) {
                SequenceImpl current = program;
                boolean encountered = false;
                StringTokenizer stok = new StringTokenizer(code, "( )", true);
                while (stok.hasMoreTokens()) {
                    final String token = stok.nextToken();
                    if ("(".equals(token)) {
                        if (encountered) {
                            SequenceImpl sequence = new SequenceImpl(current);
                            current.addElement(sequence);
                            current = sequence;
                        } else {
                            encountered = true;
                        }
                    } else if (")".equals(token)) {
                        current = current.getParent();
                    } else {
                        Instruction ins = asInstruction(token);
                        if (ins != null) {
                            current.addElement(ins);
                        }
                    }
                }
            }
        }

        return program;
    }

    private Instruction asInstruction(String token) {
        if (" ".equals(token)) {
            return null;
        }

        Instruction instruction = instructions.get(token);
        if (instruction == null) {
            instruction = asLiteral(token);
        }

        return instruction;
    }

    private Literal asLiteral(String token) {
        try {
            return new IntegerLiteral(Integer.parseInt(token));
        } catch (NumberFormatException e) {
            // Ok.
        }

        try {
            return new FloatLiteral(Float.parseFloat(token));
        } catch (NumberFormatException e) {
            // Ok.
        }

        return "TRUE".equals(token) ? new BooleanLiteral(true) :
                "FALSE".equals(token) ? new BooleanLiteral(false) :
                        createNameLiteral(token);
    }

    @Override
    public String asString(Program p) {
        StringBuilder sb = new StringBuilder();
        asString(sb, 0, p);
        return sb.toString();
    }

    private void asString(StringBuilder sb, int count, Program p) {
        if (p == null || p instanceof Sequence) {
            sb.append(count > 0 ? " (" : "(");
        }

        if (p instanceof Sequence) {
            for (Program program : ((Sequence) p).getElements()) {
                asString(sb, ++count, program);
            }
        } else if (p instanceof Instruction) {
            sb.append(" ").append(asString((Instruction) p).toUpperCase());
        }

        if (p == null || p instanceof Sequence) {
            sb.append(count > 0 ? " )" : ")");
        }
    }
}
