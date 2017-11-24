package net.orange.yap.machine.stack.instruction;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 26/07/15
 * Time: 12:01
 */
public enum IntegerInstruction implements Instruction {

    MODULO("%"),
    PRODUCT("*"),
    SUM("+"),
    DIFFERENCE("-"),
    QUOTIENT("/"),
    LESS_THAN("<"),
    EQUALS("="),
    GREATER_THAN(">"),
    DEFINE("define"),
    DUP("dup"),
    FLUSH("flush"),
    FROM_BOOLEAN("from-boolean"),
    FROM_FLOAT("from-float"),
    MAX("max"),
    MIN("min"),
    POP("pop"),
    RAND("rand"),
    ROT("rot"),
    SHOVE("shove"),
    STACK_DEPTH("stack-depth"),
    SWAP("swap"),
    YANK("yank"),
    YANK_DUP("yank-dup");

    private final String symbol;

    IntegerInstruction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Region getRegion() {
        return Region.INTEGER;
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
