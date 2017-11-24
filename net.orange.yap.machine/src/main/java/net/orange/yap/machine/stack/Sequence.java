package net.orange.yap.machine.stack;

import java.util.List;

/**
 * User: sjsmit
 * Date: 01/07/15
 * Time: 12:10
 */
public interface Sequence extends Program {

    int size();

    List<Program> getElements();
}
