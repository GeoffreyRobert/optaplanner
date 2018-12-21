package org.optaplanner.core.impl.hypersolver;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.hypersolver.switcher.HyperSolverSwitcher;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.DefaultSolver;
import org.optaplanner.core.impl.solver.random.RandomFactory;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.BasicPlumbingTermination;
import org.optaplanner.core.impl.solver.termination.Termination;

public class DefaultHyperSolver<Solution_> extends DefaultSolver<Solution_> {

    protected final HyperSolverSwitcher<Solution_> switcher;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public DefaultHyperSolver(EnvironmentMode environmentMode, RandomFactory randomFactory,
                         BestSolutionRecaller<Solution_> bestSolutionRecaller, BasicPlumbingTermination basicPlumbingTermination, Termination termination,
                              HyperSolverSwitcher<Solution_> switcher, HyperSolverScope<Solution_> solverScope) {
        super(environmentMode, randomFactory, bestSolutionRecaller, basicPlumbingTermination, termination,
                new ArrayList<>(), solverScope);
        this.switcher = switcher;
        switcher.setSolverPhaseLifecycleSupport(phaseLifecycleSupport);
    }

    public HyperSolverSwitcher<Solution_> getSwitcher() {
        return switcher;
    }

    // ************************************************************************
    // Complex getters
    // ************************************************************************

    public Solution_ getRestartSolution() {
        return ((HyperSolverScope<Solution_>) solverScope).getRestartSolution();
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        super.solvingStarted(solverScope);
        ((HyperSolverScope<Solution_>) solverScope).setRestartSolutionFromBestSolution();
        switcher.solvingStarted(solverScope);
    }

    @Override
    public void runPhases(DefaultSolverScope<Solution_> solverScope) {
        while (!termination.isSolverTerminated(solverScope)) {
            Phase<Solution_> phase = switcher.switchToNextPhase((HyperSolverScope<Solution_>)solverScope);
            // TODO gérer la récupération de data pour le evaluator
            phase.solve(solverScope);
            phaseList.add(phase);
            switcher.processNewPhase((HyperSolverScope<Solution_>)solverScope);
        }
    }

    @Override
    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {
        switcher.solvingEnded(solverScope);
        super.solvingEnded(solverScope);
    }

}
