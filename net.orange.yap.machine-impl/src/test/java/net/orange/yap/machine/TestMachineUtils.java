package net.orange.yap.machine;

import static net.orange.yap.machine.util.MachineUtils.countPoints;

/**
 * User: sjsmit
 * Date: 23/11/2017
 * Time: 16:29
 */
public class TestMachineUtils extends AbstractRuntimeTests {

    public void testCountPoints() throws Exception {
        assertEquals(2, countPoints(rt.parseProgram("")));
        assertEquals(3, countPoints(rt.parseProgram("FLOAT.-")));
        assertEquals(5, countPoints(rt.parseProgram("BOOLEAN.NOR EXEC.Y BOOLEAN.=")));
        assertEquals(11, countPoints(rt.parseProgram("( 2 3 INTEGER.* 4.1 5.2 FLOAT.+ TRUE FALSE BOOLEAN.OR )")));
    }
}
