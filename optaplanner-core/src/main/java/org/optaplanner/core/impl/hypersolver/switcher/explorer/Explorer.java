package org.optaplanner.core.impl.hypersolver.switcher.explorer;

import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.event.SolverLifecycleListener;

public interface Explorer<Solution_> extends SolverLifecycleListener<Solution_> {

    Phase<Solution_> pickNextPhase(HyperSolverScope<Solution_> solverScope);

    void resetSolution(HyperSolverScope<Solution_> solverScope);

}
