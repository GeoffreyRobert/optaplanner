package org.optaplanner.core.impl.hyperheuristic.switcher.evaluator;

import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;

public interface Evaluator<Solution_> {

    public void evaluateLastPhase(HyperHeuristicPhaseScope<Solution_> solverScope);

}
