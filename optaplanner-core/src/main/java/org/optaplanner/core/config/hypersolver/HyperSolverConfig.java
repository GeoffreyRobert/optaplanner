package org.optaplanner.core.config.hypersolver;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.hypersolver.switcher.evaluator.EvaluatorConfig;
import org.optaplanner.core.config.hypersolver.switcher.explorer.ExplorerConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.EnvironmentMode;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.recaller.BestSolutionRecallerConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.hypersolver.DefaultHyperSolver;
import org.optaplanner.core.impl.hypersolver.scope.HyperSolverScope;
import org.optaplanner.core.impl.hypersolver.switcher.HyperSolverSwitcher;
import org.optaplanner.core.impl.hypersolver.switcher.evaluator.Evaluator;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.Explorer;
import org.optaplanner.core.impl.score.director.InnerScoreDirectorFactory;
import org.optaplanner.core.impl.solver.random.RandomFactory;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.termination.BasicPlumbingTermination;
import org.optaplanner.core.impl.solver.termination.Termination;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@XStreamAlias("hyperSolver")
public class HyperSolverConfig extends SolverConfig {

    @XStreamAlias("evaluator")
    private EvaluatorConfig evaluatorConfig = null;
    @XStreamAlias("explorer")
    private ExplorerConfig explorerConfig = null;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public HyperSolverConfig() {
    }

    public HyperSolverConfig(SolverConfig inheritedConfig) {
        super(inheritedConfig);
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

    /**
     * @param configContext never null
     * @return never null
     */
    @Override
    public <Solution_> DefaultHyperSolver<Solution_> buildSolver(SolverConfigContext configContext) {
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
        HyperSolverScope<Solution_> solverScope = new HyperSolverScope<>();
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
        EvaluatorConfig evaluatorConfig_ = evaluatorConfig;
        if (evaluatorConfig_ == null) {
            evaluatorConfig_ = new EvaluatorConfig();
        }
        Evaluator evaluator = evaluatorConfig_.buildEvaluator(configPolicy);
        ExplorerConfig explorerConfig_ = explorerConfig;
        if (explorerConfig_ == null) {
            explorerConfig_ = new ExplorerConfig();
        }
        Explorer explorer = explorerConfig_.buildExplorer(configPolicy, bestSolutionRecaller, termination);
        HyperSolverSwitcher switcher = new HyperSolverSwitcher(configPolicy.getLogIndentation(),
                termination, evaluator, explorer);
        return switcher;
    }

    @Override
    public void inherit(SolverConfig inheritedConfig) {
        super.inherit(inheritedConfig);
        evaluatorConfig = ConfigUtils.inheritConfig(evaluatorConfig, ((HyperSolverConfig)inheritedConfig).getEvaluatorConfig());
        explorerConfig = ConfigUtils.inheritConfig(explorerConfig, ((HyperSolverConfig)inheritedConfig).getExplorerConfig());
    }

}
