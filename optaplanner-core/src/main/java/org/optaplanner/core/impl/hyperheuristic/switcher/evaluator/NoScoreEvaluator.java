package org.optaplanner.core.impl.hyperheuristic.switcher.evaluator;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;

public class NoScoreEvaluator implements Evaluator {

    public int evaluateLastPhase(HyperHeuristicPhaseScope phaseScope) {
        return 0;
    }

}
