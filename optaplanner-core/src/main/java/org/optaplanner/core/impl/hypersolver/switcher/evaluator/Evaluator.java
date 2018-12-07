package org.optaplanner.core.impl.hypersolver.switcher.evaluator;

import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.solver.event.SolverLifecycleListener;

public interface Evaluator<Solution_> {

    public void evaluateLastPhase(HyperSolverScope<Solution_> solverScope);

}
