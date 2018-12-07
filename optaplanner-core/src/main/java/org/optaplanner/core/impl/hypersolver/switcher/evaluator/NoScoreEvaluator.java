package org.optaplanner.core.impl.hypersolver.switcher.evaluator;

import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

public class NoScoreEvaluator<Solution_> implements Evaluator<Solution_> {

    public void evaluateLastPhase(HyperSolverScope<Solution_> solverScope) {
        solverScope.setEvaluationCount(solverScope.getEvaluationCount() + 1);
    }

}
