package org.optaplanner.core.impl.hypersolver.scope;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;

public class HyperSolverScope<Solution_> extends DefaultSolverScope {

    protected volatile Solution_ restartSolution;
    protected long evaluationCount;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

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

    // ************************************************************************
    // Calculated methods
    // ************************************************************************

    public void setRestartSolutionFromBestSolution() {
        // The workingSolution must never be the same instance as the bestSolution.
        restartSolution = (Solution_)scoreDirector.cloneSolution(bestSolution);
    }

    public void setWorkingSolutionFromRestartSolution() {
        scoreDirector.setWorkingSolution(scoreDirector.cloneSolution(restartSolution));
    }

}
