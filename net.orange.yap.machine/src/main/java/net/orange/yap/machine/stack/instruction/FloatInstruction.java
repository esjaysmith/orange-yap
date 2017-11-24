package net.orange.yap.machine.stack.instruction;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 26/07/15
 * Time: 12:01
 */
public enum FloatInstruction implements Instruction {

    MODULO("%"),
    PRODUCT("*"),
    SUM("+"),
    DIFFERENCE("-"),
    QUOTIENT("/"),
    LESSTHAN("<"),
    EQUALS("="),
    GREATERTHAN(">"),
    COS("cos"),
    DEFINE("define"),
    DUP("dup"),
    FLUSH("flush"),
    FROMBOOLEAN("fromboolean"),
    FROMINTEGER("frominteger"),
    MAX("max"),
    MIN("min"),
    POP("pop"),
    RAND("rand"),
    ROT("rot"),
    SHOVE("shove"),
    SIN("sin"),
    STACKDEPTH("stackdepth"),
    SWAP("swap"),
    TAN("tan"),
    YANK("yank"),
    YANKDUP("yankdup"),
    EXP("exp"),
    LOG("log"),
    SIGMOID("sigmoid"),
    ABS("abs"),
    GAUSSIAN("gaussian"),
    RELU("relu");

    private final String symbol;

    FloatInstruction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Region getRegion() {
        return Region.FLOAT;
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
