package net.orange.yap.machine.impl;

/**
 * User: sjsmit
 * Date: 31/12/15
 * Time: 15:54
 */
class SafeFloatStackImpl extends StackImpl<Float> {

    SafeFloatStackImpl(int maxSize) {
        super(maxSize);
    }

    private static boolean accept(Float f) {
        return !(Float.isNaN(f) || Float.isInfinite(f));
    }

    @Override
    public boolean push(Float f) {
        boolean accept = accept(f);
        return accept && super.push(f);
    }

    @Override
    public boolean push(int index, Float f) {
        boolean accept = accept(f);
        return accept && super.push(index, f);
    }
}
