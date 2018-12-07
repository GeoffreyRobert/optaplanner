package org.optaplanner.core.impl.hypersolver.switcher;

import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.hypersolver.switcher.evaluator.Evaluator;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.Explorer;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.Termination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperSolverSwitcher<Solution_> {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    protected final String logIndentation;
    protected final Termination termination;
    protected final Evaluator evaluator;
    protected final Explorer explorer;

    public HyperSolverSwitcher(String logIndentation, Termination termination,
                               Evaluator evaluator, Explorer explorer) {
        this.logIndentation = logIndentation;
        this.termination = termination;
        this.evaluator = evaluator;
        this.explorer = explorer;
    }

    public Termination getTermination() {
        return termination;
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public Explorer getExplorer() {
        return explorer;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        explorer.solvingStarted(solverScope);
    }

    public void processNewPhase(HyperSolverScope<Solution_> solverScope) {
        evaluator.evaluateLastPhase(solverScope);
        explorer.resetSolution(solverScope);
    }

    public Phase<Solution_> switchToNextPhase(HyperSolverScope<Solution_> solverScope) {
        return explorer.pickNextPhase(solverScope);
    }

    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {
        explorer.solvingEnded(solverScope);
    }

}