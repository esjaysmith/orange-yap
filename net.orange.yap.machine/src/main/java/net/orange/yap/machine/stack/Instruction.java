package net.orange.yap.machine.stack;

/**
 * User: sjsmit
 * Date: 01/07/15
 * Time: 12:10
 */
public interface Instruction extends Program {

    default Region getRegion() {
        return null;
    }

    default String getSymbol() {
        return null;
    }
}
