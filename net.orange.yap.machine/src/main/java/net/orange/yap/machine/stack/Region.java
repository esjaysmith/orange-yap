package net.orange.yap.machine.stack;

/**
 * User: sjsmit
 * Date: 01/07/15
 * Time: 11:26
 */
public enum Region {

    BOOLEAN("boolean"),
    INTEGER("integer"),
    FLOAT("float"),
    CODE("code"),
    EXEC("exec"),
    NAME("name");

    private final String symbol;

    Region(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
