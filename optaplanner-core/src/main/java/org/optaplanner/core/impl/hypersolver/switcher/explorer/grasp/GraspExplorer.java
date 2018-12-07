package org.optaplanner.core.impl.hypersolver.switcher.explorer.grasp;

import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.Explorer;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

import java.util.List;
import java.util.Map;

public class GraspExplorer<Solution_> implements Explorer<Solution_> {

    private Boolean localPhase = false;
    private Phase<Solution_> construction;
    private Phase<Solution_> local;

    public GraspExplorer() {}

    public Phase<Solution_> getConstruction() {
        return construction;
    }

    public void setConstruction(Phase<Solution_> construction) {
        this.construction = construction ;
    }

    public Phase<Solution_> getLocal() {
        return local;
    }

    public void setLocal(Phase<Solution_> local) {
        this.local = local ;
    }

    public Phase<Solution_> pickNextPhase(HyperSolverScope<Solution_> solverScope) {
        if (localPhase) {
            return local;
        } else {
            return construction;
        }
    }

    public void resetSolution(HyperSolverScope<Solution_> solverScope) {
        if (localPhase) {
            solverScope.setWorkingSolutionFromRestartSolution();
        }
        localPhase = !localPhase;
    }

    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        localPhase = false;
    }

    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {

    }

}
