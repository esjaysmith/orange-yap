package net.orange.yap.machine.stack.instruction;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 22:08
 */
public enum ExecInstruction implements Instruction {

    EQUALS("="),
    DEFINE("define"),
    DO_COUNT("do*count"),
    DO_RANGE("do*range"),
    DO_TIMES("do*times"),
    DUP("dup"),
    FLUSH("flush"),
    IF("if"),
    K("k"),
    POP("pop"),
    ROT("rot"),
    S("s"),
    SHOVE("shove"),
    STACKDEPTH("stackdepth"),
    SWAP("swap"),
    Y("y"),
    YANK("yank"),
    YANKDUP("yankdup"),
    INITRNG("initrng");

    private final String symbol;

    ExecInstruction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Region getRegion() {
        return Region.EXEC;
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
