package net.orange.yap.machine.stack.instruction;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 01/07/15
 * Time: 11:30
 */
public enum BooleanInstruction implements Instruction {

    EQUALS("="),
    AND("and"),
    DEFINE("define"),
    DUP("dup"),
    FLUSH("flush"),
    FROM_FLOAT("from-float"),
    FROM_INTEGER("from-integer"),
    NOT("not"),
    OR("or"),
    POP("pop"),
    RAND("rand"),
    ROT("rot"),
    SHOVE("shove"),
    STACK_DEPTH("stack-depth"),
    SWAP("swap"),
    YANK("yank"),
    YANK_DUP("yank-dup"),
    XOR("xor"),
    NOR("nor"),
    NAND("nand");

    private final String symbol;

    BooleanInstruction(String symbol) {
        this.symbol = symbol;
    }

    public Region getRegion() {
        return Region.BOOLEAN;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }


    @Override
    public String toString() {
        return getRegion().getSymbol() + "." + getSymbol();
    }
}
