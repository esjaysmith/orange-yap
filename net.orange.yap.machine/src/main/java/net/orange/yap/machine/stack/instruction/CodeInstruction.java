package net.orange.yap.machine.stack.instruction;

import net.orange.yap.machine.stack.Instruction;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 22:17
 */
public enum CodeInstruction implements Instruction {

    EQUALS("="),
    APPEND("append"),
    ATOM("atom"),
    CAR("car"),
    CDR("cdr"),
    CONS("cons"),
    //CONTAINER("container"),
    //CONTAINS("contains"),
    DEFINE("define"),
    DEFINITION("definition"),
    //DISCREPANCY("discrepancy"),
    DO("do"),
    DO_STAR("do*"),
    DO_COUNT("do*count"),
    DO_RANGE("do*range"),
    DO_TIMES("do*times"),
    DUP("dup"),
    //EXTRACT("extract"),
    FLUSH("flush"),
    FROMBOOLEAN("fromboolean"),
    FROMFLOAT("fromfloat"),
    FROMINTEGER("frominteger"),
    FROMNAME("fromname"),
    IF("if"),
    //INSERT("insert"),
    //INSTRUCTIONS("instructions"),
    LENGTH("length"),
    LIST("list"),
    //MEMBER("member"),
    NOOP("noop"),
    NTH("nth"),
    NTHCDR("nthcdr"),
    NULL("null"),
    POP("pop"),
    //POSITION("position"),
    QUOTE("quote"),
    //RAND("rand"),
    ROT("rot"),
    SHOVE("shove"),
    SIZE("size"),
    STACKDEPTH("stackdepth"),
    //SUBST("subst"),
    SWAP("swap"),
    YANK("yank"),
    YANKDUP("yankdup");

    private final String symbol;

    CodeInstruction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Region getRegion() {
        return Region.CODE;
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
