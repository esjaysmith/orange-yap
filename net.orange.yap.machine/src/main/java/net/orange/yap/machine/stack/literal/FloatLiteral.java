package net.orange.yap.machine.stack.literal;

import net.orange.yap.machine.stack.Literal;
import net.orange.yap.machine.stack.Region;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:55
 */
public class FloatLiteral extends AbstractLiteral<Float> implements Literal<Float> {

    public FloatLiteral(Float value) {
        super(value);
    }

    @Override
    public Region getRegion() {
        return Region.FLOAT;
    }

    @Override
    public String getSymbol() {
        return Float.toString(getConstant());
    }
}
