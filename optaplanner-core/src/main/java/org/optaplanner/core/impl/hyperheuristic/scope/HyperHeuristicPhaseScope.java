package org.optaplanner.core.impl.hyperheuristic.scope;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.impl.localsearch.scope.LocalSearchStepScope;
import org.optaplanner.core.impl.phase.scope.AbstractPhaseScope;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

public class HyperHeuristicPhaseScope<Solution_> extends AbstractPhaseScope<Solution_> {

    private HyperHeuristicStepScope<Solution_> lastCompletedStepScope;
    protected volatile Solution_ restartSolution;
    protected long evaluationCount;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public HyperHeuristicPhaseScope(DefaultSolverScope<Solution_> solverScope) {
        super(solverScope);
        restartSolution = solverScope.getWorkingSolution();
        lastCompletedStepScope = new HyperHeuristicStepScope<>(this, -1);
        lastCompletedStepScope.setTimeGradient(0.0);
    }

    public Solution_ getRestartSolution() {
        return restartSolution;
    }

    /**
     * The {@link PlanningSolution restart solution} must never be the same instance
     * as the {@link PlanningSolution working solution} or {@link PlanningSolution best
     * solution} it should be a (un)changed clone.
     * @param restartSolution never null
     */
    public void setRestartSolution(Solution_ restartSolution) {
        this.restartSolution = restartSolution;
    }

    public long getEvaluationCount() { return evaluationCount; }

    public void setEvaluationCount(long evaluationCount) { this.evaluationCount = evaluationCount; }

    @Override
    public HyperHeuristicStepScope<Solution_> getLastCompletedStepScope() {
        return  lastCompletedStepScope;
    }

    public void setLastCompletedStepScope(HyperHeuristicStepScope<Solution_> lastCompletedStepScope) {
        this.lastCompletedStepScope = lastCompletedStepScope;
    }

    // ************************************************************************
    // Calculated methods
    // ************************************************************************

    public void setRestartSolutionFromWorkingSolution() {
        restartSolution = solverScope.getScoreDirector().cloneSolution(solverScope.getWorkingSolution());
    }

    public void setRestartSolutionFromBestSolution() {
        // The workingSolution must never be the same instance as the bestSolution.
        restartSolution = solverScope.getScoreDirector().cloneSolution(solverScope.getBestSolution());
    }

    public void setWorkingSolutionFromRestartSolution() {
        solverScope.getScoreDirector().setWorkingSolution(
                solverScope.getScoreDirector().cloneSolution(restartSolution));
    }

}
