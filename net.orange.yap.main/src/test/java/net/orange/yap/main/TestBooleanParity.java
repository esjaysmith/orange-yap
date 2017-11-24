package net.orange.yap.main;

import junit.framework.TestCase;
import net.orange.yap.machine.YapRuntime;
import net.orange.yap.machine.impl.YapRuntimeFactoryImpl;
import net.orange.yap.main.evaluation.BooleanParityProblem;

/**
 * User: sjsmit
 * Date: 23/11/2017
 * Time: 16:38
 */
public class TestBooleanParity extends TestCase {

    public void testPrograms() throws Exception {
        YapRuntime rt = new YapRuntimeFactoryImpl().create();
        BooleanParityProblem problem = new BooleanParityProblem(rt, 10);
        System.out.println(problem.fitness(rt.parseProgram("EXEC.Y BOOLEAN.=")));
    }
}
