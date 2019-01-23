package org.optaplanner.core.config.hyperheuristic.switcher.explorer;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.hyperheuristic.switcher.explorer.Explorer;
import org.optaplanner.core.impl.hyperheuristic.switcher.explorer.grasp.GraspExplorer;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.termination.Termination;

import java.util.List;

public class ExplorerConfig extends AbstractConfig<ExplorerConfig> {

    @XStreamAlias("explorerType")
    private List<ExplorerType> explorerTypeList = null;

    @XStreamAlias("termination")
    private TerminationConfig terminationConfig = null;

    // ************************************************************************
    // Simple getters/setters
    // ************************************************************************

    public List<ExplorerType> getExplorerTypeList() {
        return explorerTypeList;
    }

    public void setExplorerTypeList(List<ExplorerType> explorerTypeList) {
        this.explorerTypeList = explorerTypeList;
    }

    public TerminationConfig getTerminationConfig() {
        return terminationConfig;
    }

    public void setTerminationConfig(TerminationConfig terminationConfig) {
        this.terminationConfig = terminationConfig;
    }

    // ************************************************************************
    // With methods
    // ************************************************************************

    public ExplorerConfig withEvaluationFunction(TerminationConfig terminationConfig) {
        this.terminationConfig = terminationConfig;
        return this;
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
