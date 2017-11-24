package net.orange.yap.machine;

import net.orange.yap.machine.impl.MachineInternal;
import net.orange.yap.machine.stack.literal.FloatLiteral;

/**
 * User: sjsmit
 * Date: 17/05/16
 * Time: 22:00
 */
public class TestNameBindings extends AbstractRuntimeTests {

    public void testSimpleBindings() throws Exception {
        MachineInternal mi = (MachineInternal) rt.createMachine(rt.parseProgram("( x )"));
        mi.bindings().define("x", new FloatLiteral(3.14159f));
        rt.execute(mi);
        assertEquals(1, mi.floats().size());
        assertEquals(3.14159f, mi.floats().pop());

        mi = (MachineInternal) rt.createMachine(rt.parseProgram("( x FLOAT.SIN y FLOAT.* )"));
        mi.bindings().define("x", new FloatLiteral(45f));
        mi.bindings().define("y", new FloatLiteral(.9f));
        rt.execute(mi);
        assertEquals(1, mi.floats().size());
        assertEquals(.9f * (float) Math.sin(45f), mi.floats().pop());
    }
}
