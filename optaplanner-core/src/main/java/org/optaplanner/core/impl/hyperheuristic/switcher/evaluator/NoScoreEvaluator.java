package org.optaplanner.core.impl.hyperheuristic.switcher.evaluator;

import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;

public class NoScoreEvaluator<Solution_> implements Evaluator<Solution_> {

    public void evaluateLastPhase(HyperHeuristicPhaseScope<Solution_> solverScope) {
        solverScope.setEvaluationCount(solverScope.getEvaluationCount() + 1);
    }

}
