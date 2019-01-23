package org.optaplanner.core.config.hyperheuristic;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.hyperheuristic.switcher.evaluator.EvaluatorConfig;
import org.optaplanner.core.config.hyperheuristic.switcher.evaluator.EvaluatorType;
import org.optaplanner.core.config.hyperheuristic.switcher.explorer.ExplorerConfig;
import org.optaplanner.core.config.hyperheuristic.switcher.explorer.ExplorerType;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.recaller.BestSolutionRecallerConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.hyperheuristic.DefaultHyperHeuristicPhase;
import org.optaplanner.core.impl.hyperheuristic.scope.HyperHeuristicPhaseScope;
import org.optaplanner.core.impl.hyperheuristic.switcher.HyperHeuristicSwitcher;
import org.optaplanner.core.impl.hyperheuristic.switcher.evaluator.Evaluator;
import org.optaplanner.core.impl.hyperheuristic.switcher.explorer.Explorer;
import org.optaplanner.core.impl.score.director.InnerScoreDirectorFactory;
import org.optaplanner.core.impl.solver.random.RandomFactory;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.termination.BasicPlumbingTermination;
import org.optaplanner.core.impl.solver.termination.Termination;

import java.util.ArrayList;
import java.util.Collections;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@XStreamAlias("hyperHeuristic")
public class HyperHeuristicPhaseConfig extends PhaseConfig<HyperHeuristicPhaseConfig> {

    protected HyperHeuristicType hyperHeuristicType = null;

    @XStreamAlias("evaluator")
    private EvaluatorConfig evaluatorConfig = null;
    @XStreamAlias("explorer")
    private ExplorerConfig explorerConfig = null;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public HyperHeuristicType getHyperHeuristicType() {
        return hyperHeuristicType;
    }

    public void setHyperHeuristicType(HyperHeuristicType hyperHeuristicType) {
        this.hyperHeuristicType = hyperHeuristicType;
    }

    public EvaluatorConfig getEvaluatorConfig() {
        return evaluatorConfig;
    }

    public void setEvaluatorConfig(EvaluatorConfig switcherConfig) {
        this.evaluatorConfig = switcherConfig;
    }

    public ExplorerConfig getExplorerConfig() {
        return explorerConfig;
    }

    public void setExplorerConfig(ExplorerConfig explorerConfig) {
        this.explorerConfig = explorerConfig;
    }

    // ************************************************************************
    // Builder methods
    // ************************************************************************

    @Override
    public DefaultHyperHeuristicPhase buildPhase(int phaseIndex, HeuristicConfigPolicy solverConfigPolicy,
                                                 BestSolutionRecaller bestSolutionRecaller, Termination solverTermination) {
        HeuristicConfigPolicy phaseConfigPolicy = solverConfigPolicy.createPhaseConfigPolicy();
        DefaultHyperHeuristicPhase phase = new DefaultHyperHeuristicPhase(
                phaseIndex, solverConfigPolicy.getLogIndentation(), bestSolutionRecaller,
                buildPhaseTermination(phaseConfigPolicy, solverTermination));
        phase.setSwitcher(buildSwitcher(phaseConfigPolicy, bestSolutionRecaller,
                solverTermination));
        phase.setPhaseList(new ArrayList<>());
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

    protected HyperHeuristicSwitcher buildSwitcher(HeuristicConfigPolicy configPolicy,
                                                BestSolutionRecaller bestSolutionRecaller, Termination solverTermination) {
        Evaluator evaluator = buildEvaluator(configPolicy);
        Explorer explorer = buildExplorer(configPolicy, bestSolutionRecaller, solverTermination);
        return new HyperHeuristicSwitcher(configPolicy.getLogIndentation(),
                solverTermination, evaluator, explorer);
    }

    protected Evaluator buildEvaluator(HeuristicConfigPolicy configPolicy) {
        EvaluatorConfig evaluatorConfig_;
        if (evaluatorConfig != null) {
            if (hyperHeuristicType != null) {
                throw new IllegalArgumentException("The hyperHeuristicType (" + hyperHeuristicType
                        + ") must not be configured if the evaluatorConfig (" + evaluatorConfig
                        + ") is explicitly configured.");
            }
            evaluatorConfig_ = evaluatorConfig;
        } else {
            HyperHeuristicType hyperHeuristicType_ = defaultIfNull(hyperHeuristicType, HyperHeuristicType.GRASP);
            evaluatorConfig_ = new EvaluatorConfig();
            switch (hyperHeuristicType_) {
                case GRASP:
                    evaluatorConfig_.setEvaluatorTypeList(Collections.singletonList(EvaluatorType.NO_SCORE));
                    break;
                default:
                    throw new IllegalStateException("The hyperHeuristicType (" + hyperHeuristicType_
                            + ") is not implemented.");
            }
        }
        return evaluatorConfig_.buildEvaluator(configPolicy);
    }

    protected Explorer buildExplorer (HeuristicConfigPolicy configPolicy, BestSolutionRecaller bestSolutionRecaller,
                                      Termination solverTermination) {
        ExplorerConfig explorerConfig_;
        if (explorerConfig != null) {
            if (hyperHeuristicType != null) {
                throw new IllegalArgumentException("The hyperHeuristicType (" + hyperHeuristicType
                        + ") must not be configured if the explorerConfig (" + explorerConfig
                        + ") is explicitly configured.");
            }
            explorerConfig_ = explorerConfig;
        } else {
            HyperHeuristicType hyperHeuristicType_ = defaultIfNull(hyperHeuristicType, HyperHeuristicType.GRASP);
            explorerConfig_ = new ExplorerConfig();
            switch (hyperHeuristicType_) {
                case GRASP:
                    explorerConfig_.setExplorerTypeList(Collections.singletonList(ExplorerType.PRESET));
                    break;
                default:
                    throw new IllegalStateException("The hyperHeuristicType (" + hyperHeuristicType_
                            + ") is not implemented.");
            }
        }
        return explorerConfig_.buildExplorer(configPolicy, bestSolutionRecaller, solverTermination);
    }

    @Override
    public void inherit(HyperHeuristicPhaseConfig inheritedConfig) {
        super.inherit(inheritedConfig);
        evaluatorConfig = ConfigUtils.inheritConfig(evaluatorConfig, inheritedConfig.getEvaluatorConfig());
        explorerConfig = ConfigUtils.inheritConfig(explorerConfig, inheritedConfig.getExplorerConfig());
    }

}
