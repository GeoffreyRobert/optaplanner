package org.optaplanner.core.impl.hyperheuristic.switcher.explorer;

import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.phase.event.PhaseLifecycleSupport;
import org.optaplanner.core.impl.solver.event.SolverLifecycleListener;

public interface Explorer<Solution_> extends SolverLifecycleListener<Solution_> {

    void setSolverPhaseLifecycleSupport(PhaseLifecycleSupport<Solution_> solverPhaseLifecycleSupport);

    Phase<Solution_> pickNextPhase(HyperHeuristicPhaseScope<Solution_> solverScope);

    void resetSolution(HyperHeuristicPhaseScope<Solution_> solverScope);

}
