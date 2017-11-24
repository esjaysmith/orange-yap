package net.orange.yap.machine.stack;

/**
 * The syntax of Push is simply this:
 * <p>
 * program ::= instruction | literal | ( program* )
 * <p>
 * In other words:
 * <p>
 * an instruction is a Push program
 * a literal is a Push program
 * a parenthesized sequence of zero or more Push programs is a Push program
 * <p>
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:21
 */
public interface Program {
}
