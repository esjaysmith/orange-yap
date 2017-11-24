package net.orange.yap.machine;

import net.orange.yap.machine.stack.Program;
import net.orange.yap.machine.stack.Sequence;
import net.orange.yap.machine.stack.instruction.BooleanInstruction;
import net.orange.yap.machine.stack.instruction.FloatInstruction;
import net.orange.yap.machine.stack.instruction.IntegerInstruction;
import net.orange.yap.machine.stack.literal.BooleanLiteral;
import net.orange.yap.machine.stack.literal.FloatLiteral;
import net.orange.yap.machine.stack.literal.IntegerLiteral;

import java.util.Iterator;
import java.util.List;

/**
 * User: sjsmit
 * Date: 02/08/15
 * Time: 18:56
 */
public class TestCodeParser extends AbstractRuntimeTests {

    private static void assertEmptySequence(Program p) {
        assertTrue(p instanceof Sequence);
        Sequence sequence = (Sequence) p;
        assertTrue(sequence.getElements().isEmpty());
    }

    private void assertOneDeepEmptySequence(Program p) {
        assertTrue(p instanceof Sequence);
        Sequence sequence = (Sequence) p;
        assertEquals(1, sequence.getElements().size());
        assertEmptySequence(sequence.getElements().iterator().next());
    }

    public void testEmpty() throws Exception {
        assertEmptySequence(rt.parseProgram(null));
        assertEmptySequence(rt.parseProgram(""));
        assertEmptySequence(rt.parseProgram(" "));
        assertEmptySequence(rt.parseProgram("()"));
        assertEmptySequence(rt.parseProgram("( )"));

        assertOneDeepEmptySequence(rt.parseProgram("(())"));
        assertOneDeepEmptySequence(rt.parseProgram("(( ))"));
        assertOneDeepEmptySequence(rt.parseProgram("( ( ) )"));
    }

    public void testStatement001() throws Exception {
        final String code = "( 2 3 INTEGER.* 4.1 5.2 FLOAT.+ TRUE FALSE BOOLEAN.OR )";

        Program p = rt.parseProgram(code);
        assertTrue(p instanceof Sequence);
        Sequence sequence = (Sequence) p;
        List<Program> elements = sequence.getElements();
        assertNotNull(elements);
        assertEquals(9, elements.size());

        Iterator<Program> iterator = elements.iterator();
        assertEquals(new IntegerLiteral(2), iterator.next());
        assertEquals(new IntegerLiteral(3), iterator.next());
        assertEquals(IntegerInstruction.PRODUCT, iterator.next());
        assertEquals(new FloatLiteral(4.1f), iterator.next());
        assertEquals(new FloatLiteral(5.2f), iterator.next());
        assertEquals(FloatInstruction.SUM, iterator.next());
        assertEquals(new BooleanLiteral(true), iterator.next());
        assertEquals(new BooleanLiteral(false), iterator.next());
        assertEquals(BooleanInstruction.OR, iterator.next());

        // Round-trip it.
        assertEquals(code, rt.asString(p));
    }

    public void testStatement002() throws Exception {
        final String code = "( 5 1.23 INTEGER.+ ( 4 ) INTEGER.- 5.67 FLOAT.* )";

        Program p = rt.parseProgram(code);
        assertTrue(p instanceof Sequence);
        Sequence sequence = (Sequence) p;
        List<Program> elements = sequence.getElements();
        assertNotNull(elements);
        assertEquals(7, elements.size());

        Iterator<Program> iterator = elements.iterator();
        assertEquals(new IntegerLiteral(5), iterator.next());
        assertEquals(new FloatLiteral(1.23f), iterator.next());
        assertEquals(IntegerInstruction.SUM, iterator.next());
        Program subSequence = iterator.next();
        assertTrue(subSequence instanceof Sequence);
        assertEquals(new IntegerLiteral(4), ((Sequence) subSequence).getElements().get(0));
        assertEquals(IntegerInstruction.DIFFERENCE, iterator.next());
        assertEquals(new FloatLiteral(5.67f), iterator.next());
        assertEquals(FloatInstruction.PRODUCT, iterator.next());

        // Round-trip it.
        assertEquals(code, rt.asString(p));
    }

    public void testRoundTrips() throws Exception {
        assertRoundTrip("( INTEGER.DUP INTEGER.+ )");
        assertRoundTrip("( CODE.QUOTE ( INTEGER.DUP INTEGER.+ ) CODE.DO )");
        assertRoundTrip("( DOUBLE CODE.QUOTE ( INTEGER.DUP INTEGER.+ ) CODE.DEFINE )");
        assertRoundTrip("( CODE.QUOTE ( INTEGER.DUP INTEGER.+ ) DOUBLE CODE.DEFINE )");
        assertRoundTrip("( DOUBLE EXEC.DEFINE ( INTEGER.DUP INTEGER.+ ) )");
        assertRoundTrip("( 1 INTEGER.MAX CODE.QUOTE INTEGER.* 1 CODE.DO*RANGE )");
        assertRoundTrip("( 1 INTEGER.MAX 1 EXEC.DO*RANGE INTEGER.* )");
        assertRoundTrip("( INTEGER.= CODE.QUOTE FLOAT.* CODE.QUOTE FLOAT./ CODE.IF )");
        assertRoundTrip("( INTEGER.= EXEC.IF FLOAT.* FLOAT./ )");
        assertRoundTrip("( EXEC.Y ( <BODY/CONDITION> EXEC.IF ( ) EXEC.POP ) )");
    }

    private void assertRoundTrip(String code) {
        assertEquals(code, rt.asString(rt.parseProgram(code)));
    }
}
