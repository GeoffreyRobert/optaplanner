package org.optaplanner.core.impl.hypersolver;

import java.util.List;

import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.hypersolver.switcher.HyperSolverSwitcher;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.AbstractSolver;
import org.optaplanner.core.impl.solver.random.RandomFactory;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.BasicPlumbingTermination;
import org.optaplanner.core.impl.solver.termination.Termination;

public class DefaultHyperSolver<Solution_> extends AbstractSolver<Solution_> {

    protected final HyperSolverScope<Solution_> solverScope;
    protected final HyperSolverSwitcher<Solution_> switcher;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public DefaultHyperSolver(EnvironmentMode environmentMode, RandomFactory randomFactory,
                         BestSolutionRecaller<Solution_> bestSolutionRecaller, BasicPlumbingTermination basicPlumbingTermination, Termination termination,
                         List<Phase<Solution_>> phaseList, DefaultSolverScope<Solution_> solverScope,
                         HyperSolverSwitcher<Solution_> switcher) {
        super(bestSolutionRecaller, termination, bestSolutionRecaller, basicPlumbingTermination, termination);
        this.environmentMode = environmentMode;
        this.randomFactory = randomFactory;
        this.basicPlumbingTermination = basicPlumbingTermination;
        this.solverScope = solverScope;
        this.switcher = switcher;
    }

    public HyperSolverSwitcher<Solution_> getSwitcher() {
        return switcher;
    }

    public void setDecider(HyperSolverSwitcher<Solution_> switcher) {
        this.switcher = switcher;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        super.solvingStarted(solverScope);
        switcher.solvingStarted(solverScope);
    }

    @Override
    public void runPhases(DefaultSolverScope<Solution_> solverScope) {
        while (!termination.isSolverTerminated(solverScope)) {
            switcher.processNewPhase(solverScope);
            Phase<Solution_> phase = switcher.switchToNextPhase(solverScope);
            // TODO gérer la récupération de data pour le learner
            phase.solve(solverScope);
        }
    }

    @Override
    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {
        super.solvingEnded(solverScope);
        switcher.solvingEnded(solverScope);
    }

}
