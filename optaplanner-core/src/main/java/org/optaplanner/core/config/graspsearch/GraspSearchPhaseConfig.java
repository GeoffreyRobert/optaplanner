package org.optaplanner.core.config.graspsearch;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.LocalSearchType;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.constructionheuristic.DefaultConstructionHeuristicPhase;
import org.optaplanner.core.impl.constructionheuristic.ConstructionHeuristicPhase;
import org.optaplanner.core.impl.graspsearch.GraspSearchPhase;
import org.optaplanner.core.impl.graspsearch.DefaultGraspSearchPhase;
import org.optaplanner.core.impl.localsearch.DefaultLocalSearchPhase;
import org.optaplanner.core.impl.localsearch.LocalSearchPhase;
import org.optaplanner.core.impl.solver.ChildThreadType;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.termination.Termination;

import static org.apache.commons.lang3.ObjectUtils.*;

@XStreamAlias("graspSearch")
public class GraspSearchPhaseConfig extends PhaseConfig<GraspSearchPhaseConfig> {

    // Warning: all fields are null (and not defaulted) because they can be inherited
    // and also because the input config file should match the output config file

    @XStreamAlias("constructionHeuristic")
    private ConstructionHeuristicPhaseConfig constructionHeuristicPhaseConfig = null;
    @XStreamAlias("localSearch")
    private LocalSearchPhaseConfig localSearchPhaseConfig = null;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public ConstructionHeuristicPhaseConfig getConstructionHeuristicPhaseConfig() {
        return constructionHeuristicPhaseConfig;
    }

    public void setConstructionHeuristicPhaseConfig(ConstructionHeuristicPhaseConfig constructionHeuristicPhaseConfig) {
        this.constructionHeuristicPhaseConfig = constructionHeuristicPhaseConfig;
    }

    public LocalSearchPhaseConfig getLocalSearchPhaseConfig() {
        return localSearchPhaseConfig;
    }

    public void setLocalSearchPhaseConfig(LocalSearchPhaseConfig localSearchPhaseConfig) {
        this.localSearchPhaseConfig = localSearchPhaseConfig;
    }

    // ************************************************************************
    // Builder methods
    // ************************************************************************

    // TODO BestSolutionRecaller pourrait être modifié pour fonctionner avec GRASP
    // TODO HeuristicConfigPolicy pourrait être modifié pour fonctionner avec GRASP
    @Override
    public GraspSearchPhase buildPhase(int phaseIndex, HeuristicConfigPolicy solverConfigPolicy,
                                       BestSolutionRecaller bestSolutionRecaller, Termination solverTermination) {
        HeuristicConfigPolicy phaseConfigPolicy = solverConfigPolicy.createPhaseConfigPolicy();
        DefaultGraspSearchPhase phase = new DefaultGraspSearchPhase(
                phaseIndex, solverConfigPolicy.getLogIndentation(), bestSolutionRecaller,
                buildPhaseTermination(phaseConfigPolicy, solverTermination));
        phase.setConstructionHeuristicPhase(new DefaultConstructionHeuristicPhase(
                phaseIndex, solverConfigPolicy.getLogIndentation(), bestSolutionRecaller,
                buildPhaseTermination(phaseConfigPolicy, solverTermination)));
        phase.setLocalSearchPhase(new DefaultLocalSearchPhase(
                phaseIndex, solverConfigPolicy.getLogIndentation(), bestSolutionRecaller,
                buildPhaseTermination(phaseConfigPolicy, solverTermination)));
        EnvironmentMode environmentMode = phaseConfigPolicy.getEnvironmentMode();
        if (environmentMode.isNonIntrusiveFullAsserted()) {
            phase.setAssertStepScoreFromScratch(true);
        }
        if (environmentMode.isIntrusiveFastAsserted()) {
            phase.setAssertExpectedStepScore(true);
            phase.setAssertShadowVariablesAreNotStaleAfterStep(true);
        }
        return phase;
    }

    @Override
    public void inherit(GraspSearchPhaseConfig inheritedConfig) {
        super.inherit(inheritedConfig);
        constructionHeuristicPhaseConfig = ConfigUtils.inheritConfig(
                constructionHeuristicPhaseConfig, inheritedConfig.getConstructionHeuristicPhaseConfig());
        localSearchPhaseConfig = ConfigUtils.inheritConfig(
                localSearchPhaseConfig, inheritedConfig.getLocalSearchPhaseConfig());
    }

}
