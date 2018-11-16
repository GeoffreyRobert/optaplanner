package org.optaplanner.core.impl.graspsearch.scope;

import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.impl.phase.scope.AbstractStepScope;

public class GraspSearchStepScope<Solution_> extends AbstractStepScope<Solution_> {

    private final GraspSearchPhaseScope<Solution_> phaseScope;

    private double timeGradient = Double.NaN;
    private String stepString = null;
    private Long selectedMoveTotal = null;
    private Long acceptedMoveTotal = null;
    /*
    TODO private ?Search?<Solution_> = null;
    TODO private ?Search?<Solution_> = null;
    Certainement utilisé pour calculer des métriques sur benchmark
    */

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

    // TODO public ?Search?<Solution_> getStep()

    // TODO public void setStep(?Search?<Solution_> step)

    /**
     * @return null if logging level is to high
     */
    public String getStepString() {
        return stepString;
    }

    public void setStepString(String stepString) {
        this.stepString = stepString;
    }

    /*
    TODO public ?Search?<Solution_> getUndoStep()

    TODO public void setUndoStep(?Search?<Solution_> undoStep)
    */

    public Long getSelectedMoveTotal() { return selectedMoveTotal; }

    public void setSelectedMoveTotal(Long selectedMoveTotal) { this.selectedMoveTotal = selectedMoveTotal; }

    public Long getAcceptedMoveTotal() { return acceptedMoveTotal; }

    public void setAcceptedMoveTotal(Long acceptedMoveTotal) { this.acceptedMoveTotal = acceptedMoveTotal; }

    // ************************************************************************
    // Calculated methods
    // ************************************************************************

}
