package org.optaplanner.core.impl.hyperheuristic.scope;

import org.optaplanner.core.impl.phase.scope.AbstractStepScope;

public class HyperHeuristicStepScope<Solution_> extends AbstractStepScope<Solution_> {

    private final HyperHeuristicPhaseScope<Solution_> phaseScope;

    private double timeGradient = Double.NaN;
    private String stepString = null;
    private Long selectedMoveCount = null;
    private Long acceptedMoveCount = null;

    public HyperHeuristicStepScope(HyperHeuristicPhaseScope<Solution_> phaseScope) {
        this(phaseScope, phaseScope.getNextStepIndex());
    }

    public HyperHeuristicStepScope(HyperHeuristicPhaseScope<Solution_> phaseScope, int stepIndex) {
        super(stepIndex);
        this.phaseScope = phaseScope;
    }

    @Override
    public HyperHeuristicPhaseScope<Solution_> getPhaseScope() {
        return phaseScope;
    }

    public double getTimeGradient() {
        return timeGradient;
    }

    public void setTimeGradient(double timeGradient) {
        this.timeGradient = timeGradient;
    }

    /**
     * @return null if logging level is to high
     */
    public String getStepString() {
        return stepString;
    }

    public void setStepString(String stepString) {
        this.stepString = stepString;
    }

    public Long getSelectedMoveCount() {
        return selectedMoveCount;
    }

    public void setSelectedMoveCount(Long selectedMoveCount) {
        this.selectedMoveCount = selectedMoveCount;
    }

    public Long getAcceptedMoveCount() {
        return acceptedMoveCount;
    }

    public void setAcceptedMoveCount(Long acceptedMoveCount) {
        this.acceptedMoveCount = acceptedMoveCount;
    }

    // ************************************************************************
    // Calculated methods
    // ************************************************************************

}
