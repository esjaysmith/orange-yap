package net.orange.yap.machine.stack;

/**
 * User: sjsmit
 * Date: 01/07/15
 * Time: 11:30
 */
public interface Literal<T> extends Instruction {

    T getConstant();
}
