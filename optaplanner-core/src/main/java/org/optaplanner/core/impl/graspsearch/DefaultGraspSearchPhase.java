package org.optaplanner.core.impl.graspsearch;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.impl.constructionheuristic.ConstructionHeuristicPhase;
import org.optaplanner.core.impl.constructionheuristic.event.ConstructionHeuristicPhaseLifecycleListener;
import org.optaplanner.core.impl.constructionheuristic.scope.ConstructionHeuristicPhaseScope;

import org.optaplanner.core.impl.constructionheuristic.scope.ConstructionHeuristicPhaseScope;
import org.optaplanner.core.impl.graspsearch.event.GraspSearchPhaseLifecycleListener;
import org.optaplanner.core.impl.graspsearch.scope.GraspSearchPhaseScope;
import org.optaplanner.core.impl.graspsearch.scope.GraspSearchStepScope;
import org.optaplanner.core.impl.localsearch.LocalSearchPhase;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchPhaseScope;
import org.optaplanner.core.impl.phase.AbstractPhase;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.Termination;

public class DefaultGraspSearchPhase<Solution_> extends AbstractPhase<Solution_> implements GraspSearchPhase<Solution_>,
        GraspSearchPhaseLifecycleListener<Solution_> {

    protected ConstructionHeuristicPhase<Solution_> constructionHeuristicPhase;
    protected LocalSearchPhase<Solution_> localSearchPhase;

    public DefaultGraspSearchPhase(int phaseIndex, String logIndentation,
                                   BestSolutionRecaller<Solution_> bestSolutionRecaller, Termination termination) {
        super(phaseIndex, logIndentation, bestSolutionRecaller, termination);
    }

    public ConstructionHeuristicPhase<Solution_> getConstructionHeuristicPhase() {
        return constructionHeuristicPhase;
    }

    public void setConstructionHeuristicPhase(ConstructionHeuristicPhase<Solution_> constructionHeuristicPhase) {
        this.constructionHeuristicPhase = constructionHeuristicPhase;
    }

    public LocalSearchPhase<Solution_> getLocalSearchPhase() {
        return localSearchPhase;
    }

    public void setLocalSearchPhase(LocalSearchPhase<Solution_> localSearchPhase) {
        this.localSearchPhase = localSearchPhase;
    }

    @Override
    public String getPhaseTypeString() {
        return "Grasp Search";
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void solve(DefaultSolverScope<Solution_> solverScope) {
        GraspSearchPhaseScope<Solution_> phaseScope = new GraspSearchPhaseScope<>(solverScope);
        phaseStarted(phaseScope);

        while (!termination.isPhaseTerminated(phaseScope)) {
            GraspSearchStepScope<Solution_> stepScope = new GraspSearchStepScope<>(phaseScope);
            stepScope.setTimeGradient(termination.calculatePhaseTimeGradient(phaseScope));
            stepStarted(stepScope);
            constructionHeuristicPhase.solve(solverScope);
            // TODO termination avant que l'étape soit complétée
            localSearchPhase.solve(solverScope);
            Solution_ step = stepScope.getStep();
            predictWorkingStepScore(stepScope, step);
            bestSolutionRecaller.processWorkingSolutionDuringStep(stepScope);
            stepEnded(stepScope);
            phaseScope.setLastCompletedStepScope(stepScope);
        }
        phaseEnded(phaseScope);
    }

    @Override
    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        super.solvingStarted(solverScope);
    }

    @Override
    public void phaseStarted(LocalSearchPhaseScope<Solution_> phaseScope) {
        super.phaseStarted(phaseScope);
        // TODO on garde ou pas ?
        assertWorkingSolutionInitialized(phaseScope);
    }

    @Override
    public void stepStarted(GraspSearchStepScope<Solution_> stepScope) {
        super.stepStarted(stepScope);
        stepScope.defUndoStep();
    }

    @Override
    public void stepEnded(GraspSearchStepScope<Solution_> stepScope) {
        super.stepEnded(stepScope);
        GraspSearchPhaseScope phaseScope = stepScope.getPhaseScope();
        if (logger.isDebugEnabled()) {
            logger.debug("{}    LS step ({}), time spent ({}), score ({}), {} best score ({})," +
                            " accepted/selected move count ({}/{}), picked move ({}).",
                    logIndentation,
                    stepScope.getStepIndex(),
                    phaseScope.calculateSolverTimeMillisSpentUpToNow(),
                    stepScope.getScore(),
                    (stepScope.getBestScoreImproved() ? "new" : "   "), phaseScope.getBestScore(),
                    stepScope.getAcceptedMoveTotal(),
                    stepScope.getSelectedMoveTotal(),
                    stepScope.getStepString());
        }
    }

    @Override
    public void phaseEnded(LocalSearchPhaseScope<Solution_> phaseScope) {
        super.phaseEnded(phaseScope);
        phaseScope.endingNow();
        logger.info("{}Local Search phase ({}) ended: time spent ({}), best score ({}),"
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
        super.solvingEnded(solverScope);
    }

}
