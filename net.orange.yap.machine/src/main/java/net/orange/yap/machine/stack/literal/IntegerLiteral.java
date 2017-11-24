package net.orange.yap.machine.stack.literal;

import net.orange.yap.machine.stack.Literal;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:55
 */
public class IntegerLiteral extends AbstractLiteral<Integer> implements Literal<Integer> {

    public IntegerLiteral(Integer value) {
        super(value);
    }

    @Override
    public Region getRegion() {
        return Region.INTEGER;
    }

    @Override
    public String getSymbol() {
        return Integer.toString(getConstant());
    }
}
