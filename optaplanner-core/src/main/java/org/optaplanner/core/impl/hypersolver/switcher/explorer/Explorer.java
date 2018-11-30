package org.optaplanner.core.impl.hypersolver.switcher.explorer;

import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.event.SolverLifecycleListener;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

import java.util.List;

public interface Explorer<Solution_> extends SolverLifecycleListener<Solution_> {

    public Phase<Solution_> pickNextPhase(DefaultSolverScope<Solution_> solverScope, List<Phase<Solution_>> phases);

    public void resetSolution(DefaultSolverScope<Solution_> solverScope);

}
