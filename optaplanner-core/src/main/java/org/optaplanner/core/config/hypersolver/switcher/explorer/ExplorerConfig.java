package org.optaplanner.core.config.hypersolver.switcher.explorer;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.hypersolver.switcher.evaluator.EvaluatorConfig;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.Explorer;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.grasp.GraspExplorer;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.termination.PhaseToSolverTerminationBridge;
import org.optaplanner.core.impl.solver.termination.Termination;

public class ExplorerConfig extends AbstractConfig<ExplorerConfig> {

    @XStreamAlias("termination")
    private TerminationConfig terminationConfig = null;

    // ************************************************************************
    // Constructors and simple getters/setters
    // ************************************************************************

    public TerminationConfig getTerminationConfig() {
        return terminationConfig;
    }

    public void setTerminationConfig(TerminationConfig terminationConfig) {
        this.terminationConfig = terminationConfig;
    }

    // ************************************************************************
    // Builder methods
    // ************************************************************************

    public Explorer buildExplorer(HeuristicConfigPolicy configPolicy,
                                  BestSolutionRecaller bestSolutionRecaller,
                                  Termination solverTermination) {
        return new GraspExplorer(configPolicy, bestSolutionRecaller, solverTermination);
    }

    @Override
    public void inherit(ExplorerConfig inheritedConfig) {
        terminationConfig = ConfigUtils.inheritConfig(terminationConfig, inheritedConfig.getTerminationConfig());
    }

}
