package org.optaplanner.core.config.hypersolver.switcher.explorer;

import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.hypersolver.switcher.evaluator.EvaluatorConfig;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.Explorer;
import org.optaplanner.core.impl.hypersolver.switcher.explorer.grasp.GraspExplorer;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.termination.Termination;

public class ExplorerConfig extends AbstractConfig<ExplorerConfig> {

    public ExplorerConfig() {};

    @Override
    public void inherit(ExplorerConfig inheritedConfig) {

    }

    public Explorer buildExplorer(HeuristicConfigPolicy configPolicy,
                                  BestSolutionRecaller bestSolutionRecaller,
                                  Termination termination) {
        return new GraspExplorer(configPolicy, bestSolutionRecaller, termination);
    }

}
