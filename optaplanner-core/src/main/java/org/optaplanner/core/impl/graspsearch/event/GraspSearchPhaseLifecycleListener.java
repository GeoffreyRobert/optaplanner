package org.optaplanner.core.impl.graspsearch.event;

import org.optaplanner.core.impl.localsearch.scope.LocalSearchPhaseScope;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchStepScope;
import org.optaplanner.core.impl.solver.event.SolverLifecycleListener;

public interface GraspSearchPhaseLifecycleListener<Solution_> extends SolverLifecycleListener<Solution_> {

    void phaseStarted(LocalSearchPhaseScope<Solution_> phaseScope);

    void stepStarted(LocalSearchStepScope<Solution_> stepScope);

    void stepEnded(LocalSearchStepScope<Solution_> stepScope);

    void phaseEnded(LocalSearchPhaseScope<Solution_> phaseScope);

}
