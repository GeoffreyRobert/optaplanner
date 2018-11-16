package org.optaplanner.core.impl.graspsearch.scope;

import org.optaplanner.core.impl.phase.scope.AbstractPhaseScope;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;


public class GraspSearchPhaseScope<Solution_> extends AbstractPhaseScope<Solution_> {

    private GraspSearchStepScope<Solution_> lastCompletedStepScope;

    public GraspSearchPhaseScope(DefaultSolverScope<Solution_> solverScope) {
        super(solverScope);
        lastCompletedStepScope = new GraspSearchStepScope<>(this, -1);
        lastCompletedStepScope.setTimeGradient(0.0);
    }

    @Override
    public GraspSearchStepScope<Solution_> getLastCompletedStepScope() {
        return lastCompletedStepScope;
    }

    public void setLastCompletedStepScope(GraspSearchStepScope<Solution_> lastCompletedStepScope) {
        this.lastCompletedStepScope = lastCompletedStepScope;
    }

    // ************************************************************************
    // Calculated methods
    // ************************************************************************

}
