package net.orange.yap.machine;

/**
 * The Factory to obtain a YapRuntime.
 * <p>
 * User: sjsmit
 * Date: 04/08/15
 * Time: 00:00
 *
 * @see YapRuntime
 */
public interface YapRuntimeFactory {

    int getMaximumProgramPoints();

    int getMaximumExecutionInstructions();

    /**
     * Create a new instance of the runtime as configured by this factory.
     *
     * @return A new runtime instance.
     */
    YapRuntime create();
}
