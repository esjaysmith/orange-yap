package net.orange.yap.machine.stack.literal;

import net.orange.yap.machine.stack.Literal;

import java.util.Objects;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 14:55
 */
abstract class AbstractLiteral<T> implements Literal<T> {

    private final T value;

    protected AbstractLiteral(T value) {
        this.value = value;
    }

    @Override
    public T getConstant() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractLiteral)) {
            return false;
        }

        AbstractLiteral other = (AbstractLiteral) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
