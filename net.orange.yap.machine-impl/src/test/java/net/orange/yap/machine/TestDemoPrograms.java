package net.orange.yap.machine;

import java.util.Arrays;

/**
 * User: sjsmit
 * Date: 06/08/15
 * Time: 12:57
 */
public class TestDemoPrograms extends AbstractRuntimeTests {

    public void testSimpleExamples() throws Exception {
        String code = "( 2 3 INTEGER.* 4.1 5.2 FLOAT.+ TRUE FALSE BOOLEAN.OR )";
        Machine m = rt.createMachine(rt.parseProgram(code));
        rt.execute(m);
        /*
        BOOLEAN STACK: ( TRUE )
        CODE STACK: ( ( 2 3 INTEGER.* 4.1 5.2 FLOAT.+ TRUE FALSE BOOLEAN.OR ) )
        FLOAT STACK: ( 9.3 )
        INTEGER STACK: ( 6 )
         */
        assertTrue(Arrays.equals(new Boolean[]{true}, m.booleans().toArray()));
        assertEquals(1, m.listCodeItems().size());
        assertEquals(code, rt.asString(m.listCodeItems().iterator().next()));
        // Floating point arithmetic
        assertEquals(1, m.floats().size());
        assertEquals(9.3f, m.floats().peek(), 0.00001f);
        assertTrue(Arrays.equals(new Integer[]{6}, m.integers().toArray()));

        code = "( 5 1.23 INTEGER.+ ( 4 ) INTEGER.- 5.67 FLOAT.* )";
        m = rt.createMachine(rt.parseProgram(code));
        rt.execute(m);
        /*
        CODE STACK: ( ( 5 1.23 INTEGER.+ ( 4 ) INTEGER.- 5.67 FLOAT.* ) )
        FLOAT STACK: ( 6.9741 )
        INTEGER STACK: ( 1 )
         */
        assertEquals(1, m.listCodeItems().size());
        assertEquals(code, rt.asString(m.listCodeItems().iterator().next()));
        assertEquals(1, m.floats().size());
        assertEquals(6.9741f, m.floats().peek(), 0.00001f);
        assertTrue(Arrays.equals(new Integer[]{1}, m.integers().toArray()));

        code = "( 5 INTEGER.DUP INTEGER.+ )";
        m = rt.createMachine(rt.parseProgram(code));
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{10}, m.integers().toArray()));

        code = "( 5 CODE.QUOTE ( INTEGER.DUP INTEGER.+ ) CODE.DO )";
        m = rt.createMachine(rt.parseProgram(code));
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{10}, m.integers().toArray()));

        code = "( 5 DOUBLE CODE.QUOTE ( INTEGER.DUP INTEGER.+ ) CODE.DEFINE DOUBLE )";
        m = rt.createMachine(rt.parseProgram(code));
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{10}, m.integers().toArray()));

        code = "( 5 CODE.QUOTE ( INTEGER.DUP INTEGER.+ ) DOUBLE CODE.DEFINE DOUBLE )";
        m = rt.createMachine(rt.parseProgram(code));
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{10}, m.integers().toArray()));

        code = "( 5 DOUBLE EXEC.DEFINE ( INTEGER.DUP INTEGER.+ ) DOUBLE )";
        m = rt.createMachine(rt.parseProgram(code));
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{10}, m.integers().toArray()));
    }

    public void testFactorialExamples() throws Exception {
        String code = "( CODE.QUOTE ( INTEGER.POP 1 ) " +
                "  CODE.QUOTE ( CODE.DUP INTEGER.DUP 1 INTEGER.- CODE.DO INTEGER.* ) " +
                "  INTEGER.DUP 2 INTEGER.< CODE.IF )";
        Machine m = rt.createMachine(rt.parseProgram(code));
        m.integers().push(5);
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{120}, m.integers().toArray()));

        code = "( 1 INTEGER.MAX CODE.QUOTE INTEGER.* 1 CODE.DO*RANGE)";
        m = rt.createMachine(rt.parseProgram(code));
        m.integers().push(5);
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{120}, m.integers().toArray()));

        code = "( 1 INTEGER.MAX 1 EXEC.DO*RANGE INTEGER.* )";
        m = rt.createMachine(rt.parseProgram(code));
        m.integers().push(5);
        rt.execute(m);
        assertTrue(Arrays.equals(new Integer[]{120}, m.integers().toArray()));

    }
}
