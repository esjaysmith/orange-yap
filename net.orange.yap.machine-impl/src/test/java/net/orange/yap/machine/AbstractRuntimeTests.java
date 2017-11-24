package net.orange.yap.machine;

import junit.framework.TestCase;
import net.orange.yap.machine.impl.YapRuntimeFactoryImpl;


/**
 * User: sjsmit
 * Date: 06/08/15
 * Time: 12:57
 */
abstract class AbstractRuntimeTests extends TestCase {

    YapRuntime rt;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        YapRuntimeFactory factory = new YapRuntimeFactoryImpl();
        this.rt = factory.create();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.rt = null;
    }
}
