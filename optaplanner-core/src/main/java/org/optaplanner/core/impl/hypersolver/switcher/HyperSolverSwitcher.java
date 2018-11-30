package org.optaplanner.core.impl.hypersolver.switcher;

import org.optaplanner.core.impl.hypersolver.switcher.learner.Learner;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.Explorer;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.Termination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HyperSolverSwitcher<Solution_> {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    protected final String logIndentation;
    protected final Termination termination;
    protected final Learner learner;
    protected final Explorer explorer;

    public HyperSolverSwitcher(String logIndentation, Termination termination,
                               Learner learner, Explorer explorer) {
        this.logIndentation = logIndentation;
        this.termination = termination;
        this.learner = learner;
        this.explorer = explorer;
    }

    public Termination getTermination() {
        return termination;
    }

    public Learner getLearner() {
        return learner;
    }

    public Explorer getExplorer() {
        return explorer;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        learner.solvingStarted(solverScope);
        explorer.solvingStarted(solverScope);
    }

    public void processNewPhase(DefaultSolverScope<Solution_> solverScope) {
        learner.learnOnLastPhase(solverScope);
        explorer.resetSolution(solverScope);
    }

    public Phase<Solution_> switchToNextPhase(DefaultSolverScope<Solution_> solverScope) {
        List<Phase> phases = learner.proposePhases(solverScope);
        return explorer.pickNextPhase(solverScope, phases);
    }

    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {
        learner.solvingEnded(solverScope);
        explorer.solvingEnded(solverScope);
    }

}