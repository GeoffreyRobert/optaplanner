package org.optaplanner.core.impl.hypersolver.switcher.learner;

import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.solver.event.SolverLifecycleListener;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

import java.util.List;

public interface Learner<Solution_> extends SolverLifecycleListener<Solution_>{

    public void initialize(DefaultSolverScope<Solution_> solverScope);

    public void learnOnLastPhase(DefaultSolverScope<Solution_> solverScope);

    public List<Phase<Solution_>> proposePhases(DefaultSolverScope<Solution_> solverScope);
}
