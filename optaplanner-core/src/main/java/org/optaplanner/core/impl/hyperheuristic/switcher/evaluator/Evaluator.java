package org.optaplanner.core.impl.hyperheuristic.switcher.evaluator;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;

public interface Evaluator {

    int evaluateLastPhase(HyperHeuristicPhaseScope phaseScope);

}
