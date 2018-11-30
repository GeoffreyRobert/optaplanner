package org.optaplanner.core.impl.hypersolver.switcher.learner;

import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

public class NoProposalLearner<Solution_> implements Learner<Solution_> {

    public NoProposalLearner() {};

    @Override
    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {

    }

    public void initialize(DefaultSolverScope<Solution_> solverScope) {};

    public void learnOnLastPhase(DefaultSolverScope<Solution_> solverScope) {};

    @Override
    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {

    }
}
