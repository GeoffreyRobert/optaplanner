package org.optaplanner.core.config.hypersolver;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.domain.ScanAnnotatedClassesConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.random.RandomType;
import org.optaplanner.core.config.solver.recaller.BestSolutionRecallerConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.domain.entity.descriptor.EntityDescriptor;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.hypersolver.DefaultHyperSolver;
import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.hypersolver.switcher.HyperSolverSwitcher;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.score.definition.ScoreDefinition;
import org.optaplanner.core.impl.score.director.InnerScoreDirectorFactory;
import org.optaplanner.core.impl.solver.DefaultSolver;
import org.optaplanner.core.impl.solver.random.DefaultRandomFactory;
import org.optaplanner.core.impl.solver.random.RandomFactory;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.scope.DefaultSolverScope;
import org.optaplanner.core.impl.solver.termination.BasicPlumbingTermination;
import org.optaplanner.core.impl.solver.termination.Termination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@XStreamAlias("hyperSolver")
public class HyperSolverConfig extends SolverConfig {

    @XStreamAlias("switcher")
    private SwitcherConfig switcherConfig;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public HyperSolverConfig() {
    }

    public HyperSolverConfig(SolverConfig inheritedConfig) {
        super(inheritedConfig);
    }

    public SwitcherConfig getSwitcherConfig() {
        return switcherConfig;
    }

    public void setSwitcherConfig(SwitcherConfig switcherConfig) {
        this.switcherConfig = switcherConfig;
    }

    // ************************************************************************
    // Builder methods
    // ************************************************************************

    /**
     * @param configContext never null
     * @return never null
     */
    @Override
    public <Solution_> Solver<Solution_> buildSolver(SolverConfigContext configContext) {
        configContext.validate();
        EnvironmentMode environmentMode_ = determineEnvironmentMode();
        boolean daemon_ = defaultIfNull(daemon, false);

        RandomFactory randomFactory = buildRandomFactory(environmentMode_);
        Integer moveThreadCount_ = resolveMoveThreadCount();
        SolutionDescriptor<Solution_> solutionDescriptor = buildSolutionDescriptor(configContext);
        ScoreDirectorFactoryConfig scoreDirectorFactoryConfig_
                = scoreDirectorFactoryConfig == null ? new ScoreDirectorFactoryConfig()
                : scoreDirectorFactoryConfig;
        InnerScoreDirectorFactory<Solution_> scoreDirectorFactory = scoreDirectorFactoryConfig_.buildScoreDirectorFactory(
                configContext, environmentMode_, solutionDescriptor);
        boolean constraintMatchEnabledPreference = environmentMode_.isAsserted();
        DefaultSolverScope<Solution_> solverScope = new DefaultSolverScope<>();
        solverScope.setScoreDirector(scoreDirectorFactory.buildScoreDirector(true, constraintMatchEnabledPreference));

        BestSolutionRecaller<Solution_> bestSolutionRecaller = new BestSolutionRecallerConfig()
                .buildBestSolutionRecaller(environmentMode_);
        HeuristicConfigPolicy configPolicy = new HeuristicConfigPolicy(environmentMode_,
                moveThreadCount_, moveThreadBufferSize, threadFactoryClass,
                scoreDirectorFactory);
        TerminationConfig terminationConfig_ = super.getTerminationConfig() == null ? new TerminationConfig()
                : super.getTerminationConfig();
        BasicPlumbingTermination basicPlumbingTermination = new BasicPlumbingTermination(daemon_);
        Termination termination = terminationConfig_.buildTermination(configPolicy, basicPlumbingTermination);
        HyperSolverSwitcher<Solution_> switcher = buildSwitcher(configPolicy, bestSolutionRecaller, termination);
        return new DefaultHyperSolver<>(environmentMode_, randomFactory,
                bestSolutionRecaller, basicPlumbingTermination, termination, switcher, solverScope);
    }

    protected <Solution_> HyperSolverSwitcher<Solution_> buildSwitcher(HeuristicConfigPolicy configPolicy,
            BestSolutionRecaller bestSolutionRecaller, Termination termination) {

    }

    @Override
    public void inherit(SolverConfig inheritedConfig) {
        super.inherit(inheritedConfig);
        switcherConfig = ConfigUtils.inheritConfig(switcherConfig, ((HyperSolverConfig)inheritedConfig).getSwitcherConfig())
    }

}
