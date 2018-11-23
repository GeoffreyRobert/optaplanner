package org.optaplanner.core.impl.graspsearch.scope;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.impl.phase.scope.AbstractStepScope;

public class GraspSearchStepScope<Solution_> extends AbstractStepScope<Solution_> {

    private final GraspSearchPhaseScope<Solution_> phaseScope;

    private double timeGradient = Double.NaN;
    private Solution_ step = null;
    private String stepString = null;
    private Solution_ undoStep = null;
    private Long selectedMoveTotal = null;
    private Long acceptedMoveTotal = null;
    private boolean isLocal;

    public GraspSearchStepScope(GraspSearchPhaseScope<Solution_> phaseScope) {
        this(phaseScope, phaseScope.getNextStepIndex());
    }

    public GraspSearchStepScope(GraspSearchPhaseScope<Solution_> phaseScope, int stepIndex) {
        super(stepIndex);
        this.phaseScope = phaseScope;
    }

    @Override
    public GraspSearchPhaseScope<Solution_> getPhaseScope() {
        return phaseScope;
    }

    public double getTimeGradient() {
        return timeGradient;
    }

    public void setTimeGradient(double timeGradient) {
        this.timeGradient = timeGradient;
    }

    public Solution_ getStep() { return step; }

    public void setStep(Solution_ step) { this.step = step; }

    /**
     * @return null if logging level is to high
     */
    public String getStepString() {
        return stepString;
    }

    public void setStepString(String stepString) {
        this.stepString = stepString;
    }

    public Solution_ getUndoStep() { return undoStep; }

    public void setUndoStep(Solution_ undoStep) { this.undoStep = undoStep; }

    public void defUndoStep() { phaseScope.getSolverScope().getWorkingSolution(); }

    public Long getSelectedMoveTotal() { return selectedMoveTotal; }

    public void setSelectedMoveTotal(Long selectedMoveTotal) { this.selectedMoveTotal = selectedMoveTotal; }

    public Long getAcceptedMoveTotal() { return acceptedMoveTotal; }

    public void setAcceptedMoveTotal(Long acceptedMoveTotal) { this.acceptedMoveTotal = acceptedMoveTotal; }

    // ************************************************************************
    // Calculated methods
    // ************************************************************************

}
