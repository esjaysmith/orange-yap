package net.orange.yap.machine.stack.literal;

import net.orange.yap.machine.stack.Literal;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:55
 */
public class NameLiteral extends AbstractLiteral<String> implements Literal<String> {

    public NameLiteral(String value) {
        super(value);
    }

    @Override
    public Region getRegion() {
        return Region.NAME;
    }

    @Override
    public String getSymbol() {
        return getConstant();
    }
}
