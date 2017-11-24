package net.orange.yap.machine.stack.literal;

import net.orange.yap.machine.stack.Literal;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:55
 */
public class BooleanLiteral extends AbstractLiteral<Boolean> implements Literal<Boolean> {

    public BooleanLiteral(Boolean value) {
        super(value);
    }

    @Override
    public Region getRegion() {
        return Region.BOOLEAN;
    }

    @Override
    public String getSymbol() {
        return getConstant() ? "true" : "false";
    }
}
