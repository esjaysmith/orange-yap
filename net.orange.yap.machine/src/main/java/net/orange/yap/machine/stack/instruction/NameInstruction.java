package net.orange.yap.machine.stack.instruction;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 28/07/15
 * Time: 19:19
 */
public enum NameInstruction implements Instruction {

    EQUALS("="),
    DUP("dup"),
    FLUSH("flush"),
    POP("pop"),
    QUOTE("quote"),
    RAND("rand"),
    RANDBOUNDNAME("randboundname"),
    ROT("rot"),
    SHOVE("shove"),
    STACKDEPTH("stackdepth"),
    SWAP("swap"),
    YANK("yank"),
    YANKDUP("yankdup");

    private final String symbol;

    NameInstruction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Region getRegion() {
        return Region.NAME;
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

