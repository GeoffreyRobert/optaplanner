package org.optaplanner.core.impl.hypersolver.switcher.explorer.grasp;

import org.optaplanner.core.impl.hypersolver.switcher.explorer.Explorer;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

import java.util.List;

public class GraspExplorer<Solution_> implements Explorer<Solution_> {

    private Boolean local = false;
    private Solution_ restartSolution;
    private Phase<Solution_> construction;
    private Phase<Solution_> localSearch;

    public GraspExplorer() {}

    public void setRestartSolution(Solution_ restartSolution) {
        this.restartSolution = restartSolution;
    }

    public void setConstruction(Phase<Solution_> construction) {
        this.construction = construction;
    }

    public void setLocalSearch(Phase<Solution_> localSearch) {
        this.localSearch = localSearch;
    }

    @Override
    public Phase<Solution_> pickNextPhase(DefaultSolverScope<Solution_> solverScope, List<Phase<Solution_>> phases) {
        if (local) {
            return localSearch;
        } else {
            return construction;
        }
    }

    public void resetSolution(DefaultSolverScope<Solution_> solverScope) {
        if (local) {
            solverScope.getScoreDirector().setWorkingSolution(restartSolution);
        }
        local = !local;
    }

    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {

    }

    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {

    }

}
