package org.optaplanner.core.impl.hyperheuristic.switcher.explorer.grasp;

import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType;
import org.optaplanner.core.config.constructionheuristic.placer.QueuedEntityPlacerConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.heuristic.selector.common.SelectionCacheType;
import org.optaplanner.core.config.heuristic.selector.common.SelectionOrder;
import org.optaplanner.core.config.heuristic.selector.entity.EntitySelectorConfig;
import org.optaplanner.core.config.heuristic.selector.entity.EntitySorterManner;
import org.optaplanner.core.config.heuristic.selector.move.MoveSelectorConfig;
import org.optaplanner.core.config.heuristic.selector.move.generic.ChangeMoveSelectorConfig;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;
import org.optaplanner.core.impl.hyperheuristic.switcher.explorer.Explorer;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.phase.event.PhaseLifecycleSupport;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.Termination;

import java.util.Collections;

public class GraspExplorer<Solution_> implements Explorer<Solution_> {

    private Boolean localPhase = false;
    private Phase<Solution_> construction;
    private Phase<Solution_> local;

    public GraspExplorer(HeuristicConfigPolicy configPolicy,
                         BestSolutionRecaller bestSolutionRecaller,
                         Termination solverTermination) {
        configPolicy.setEntitySorterManner(EntitySorterManner.NONE);
        ConstructionHeuristicPhaseConfig constructionHeuristicPhaseConfig = new ConstructionHeuristicPhaseConfig();
        QueuedEntityPlacerConfig queuedEntityPlacerConfig = new QueuedEntityPlacerConfig();
        EntitySelectorConfig entitySelectorConfig = new EntitySelectorConfig();
        entitySelectorConfig.setCacheType(SelectionCacheType.PHASE);
        entitySelectorConfig.setSelectionOrder(SelectionOrder.SHUFFLED);
        queuedEntityPlacerConfig.setEntitySelectorConfig(entitySelectorConfig);
        constructionHeuristicPhaseConfig.setEntityPlacerConfig(queuedEntityPlacerConfig);
        this.construction = constructionHeuristicPhaseConfig.buildPhase(1, configPolicy,
                bestSolutionRecaller, solverTermination);
        LocalSearchPhaseConfig localSearchPhaseConfig = new LocalSearchPhaseConfig();
        localSearchPhaseConfig.setTerminationConfig(new TerminationConfig().withUnimprovedStepCountLimit(10));
        this.local = localSearchPhaseConfig.buildPhase(2, configPolicy,
                bestSolutionRecaller, solverTermination);
    }

    public Phase<Solution_> getConstruction() {
        return construction;
    }

    public void setConstruction(Phase<Solution_> construction) {
        this.construction = construction ;
    }

    public Phase<Solution_> getLocal() {
        return local;
    }

    public void setLocal(Phase<Solution_> local) {
        this.local = local ;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public void setSolverPhaseLifecycleSupport(PhaseLifecycleSupport<Solution_> solverPhaseLifecycleSupport) {
        construction.setSolverPhaseLifecycleSupport(solverPhaseLifecycleSupport);
        local.setSolverPhaseLifecycleSupport(solverPhaseLifecycleSupport);
    }

    public Phase<Solution_> pickNextPhase(HyperHeuristicPhaseScope<Solution_> solverScope) {
        if (localPhase) {
            return local;
        } else {
            return construction;
        }
    }

    public void resetSolution(HyperHeuristicPhaseScope<Solution_> solverScope) {
        if (localPhase) {
            solverScope.setWorkingSolutionFromRestartSolution();
        }
        localPhase = !localPhase;
    }

    public void solvingStarted(DefaultSolverScope<Solution_> solverScope) {
        localPhase = false;
        construction.solvingStarted(solverScope);
        local.solvingStarted(solverScope);
    }

    public void solvingEnded(DefaultSolverScope<Solution_> solverScope) {

    }

}