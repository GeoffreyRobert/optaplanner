package org.optaplanner.core.impl.hyperheuristic;

import org.optaplanner.core.impl.hyperheuristic.event.HyperHeuristicLifecycleListener;
import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;
import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicStepScope;
import org.optaplanner.core.impl.hyperheuristic.switcher.HyperHeuristicSwitcher;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchPhaseScope;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchStepScope;
import org.optaplanner.core.impl.phase.AbstractPhase;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.phase.event.PhaseLifecycleSupport;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.Termination;

import java.util.List;

public class DefaultHyperHeuristicPhase<Solution_> extends AbstractPhase<Solution_>
        implements HyperHeuristicPhase<Solution_>, HyperHeuristicLifecycleListener<Solution_> {

    protected HyperHeuristicPhaseScope<Solution_> phaseScope;
    protected List<Phase<Solution_>> phaseList;
    protected HyperHeuristicSwitcher<Solution_> switcher;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public DefaultHyperHeuristicPhase(int phaseIndex, String logIndentation,
                                      BestSolutionRecaller<Solution_> bestSolutionRecaller, Termination termination) {
        super(phaseIndex, logIndentation, bestSolutionRecaller, termination);
    }

    public List<Phase<Solution_>> getPhaseList() {
        return phaseList;
    }

    public void setPhaseList(List<Phase<Solution_>> phaseList) {
        this.phaseList = phaseList;
    }

    public HyperHeuristicSwitcher<Solution_> getSwitcher() {
        return switcher;
    }

    public void setSwitcher(HyperHeuristicSwitcher<Solution_> switcher) {
        this.switcher = switcher;
    }

    public Solution_ getRestartSolution() {
        return phaseScope.getRestartSolution();
    }

    @Override
    public void setSolverPhaseLifecycleSupport(PhaseLifecycleSupport<Solution_> solverPhaseLifecycleSupport) {
        super.setSolverPhaseLifecycleSupport(solverPhaseLifecycleSupport);
        switcher.setSolverPhaseLifecycleSupport(solverPhaseLifecycleSupport);
    }

    @Override
    public String getPhaseTypeString() {
        return "Hyper Heuristic";
    }

    public HyperHeuristicPhaseScope<Solution_> getPhaseScope() {
        return phaseScope;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void solve(DefaultSolverScope<Solution_> solverScope) {
        HyperHeuristicPhaseScope<Solution_> phaseScope = new HyperHeuristicPhaseScope<>(solverScope);
        phaseStarted(phaseScope);

        while (!termination.isPhaseTerminated(phaseScope)) {
            HyperHeuristicStepScope<Solution_> stepScope = new HyperHeuristicStepScope<>(phaseScope);
            stepScope.setTimeGradient(termination.calculatePhaseTimeGradient(phaseScope));
            stepStarted(stepScope);
            Phase<Solution_> phase = switcher.switchToNextPhase(phaseScope);
            // TODO gérer la récupération de data pour le evaluator
            phase.solve(solverScope);
            phaseList.add(phase);
            switcher.processNewPhase(phaseScope);
            stepEnded(stepScope);
            phaseScope.setLastCompletedStepScope(stepScope);
        }
        phaseEnded(phaseScope);
    }

    @Override
    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        super.solvingStarted(solverScope);
        switcher.solvingStarted(solverScope);
    }

    public void phaseStarted(HyperHeuristicPhaseScope<Solution_> phaseScope) {
        super.phaseStarted(phaseScope);
        phaseScope.setRestartSolutionFromWorkingSolution();
        switcher.phaseStarted(phaseScope);
    }

    public void phaseEnded(HyperHeuristicPhaseScope<Solution_> phaseScope) {
        super.phaseEnded(phaseScope);
        switcher.phaseEnded(phaseScope);
        phaseScope.endingNow();
        logger.info("{}Hyper Heuristic phase ({}) ended: time spent ({}), best score ({}),"
                        + " score calculation speed ({}/sec), step total ({}).",
                logIndentation,
                phaseIndex,
                phaseScope.calculateSolverTimeMillisSpentUpToNow(),
                phaseScope.getBestScore(),
                phaseScope.getPhaseScoreCalculationSpeed(),
                phaseScope.getNextStepIndex());
    }

    @Override
    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {
        switcher.solvingEnded(solverScope);
        super.solvingEnded(solverScope);
    }

}
