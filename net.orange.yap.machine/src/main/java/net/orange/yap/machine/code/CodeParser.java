package net.orange.yap.machine.code;

import net.orange.yap.machine.stack.Program;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 17:54
 */
public interface CodeParser {

    Program fromString(String code);
}
