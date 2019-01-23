package org.optaplanner.core.config.hyperheuristic.switcher.explorer;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.config.hyperheuristic.switcher.evaluator.EvaluatorType;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.hyperheuristic.switcher.explorer.Explorer;
import org.optaplanner.core.impl.hyperheuristic.switcher.explorer.grasp.GraspExplorer;
import org.optaplanner.core.impl.solver.recaller.BestSolutionRecaller;
import org.optaplanner.core.impl.solver.termination.Termination;

import java.util.ArrayList;
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
        List<Explorer> explorerList = new ArrayList<>();
        if (explorerTypeList != null && explorerTypeList.contains(ExplorerType.PRESET)) {
            GraspExplorer explorer = new GraspExplorer(configPolicy, bestSolutionRecaller, solverTermination);
            explorerList.add(explorer);
        }
        if (explorerList.size() == 1) {
            return explorerList.get(0);
        } else if (explorerList.size() > 1) {
            throw new IllegalArgumentException("The explorer does not yet support composition.\n");
            // TODO Implement CompositeExplorer
            // return new CompositeExplorer(explorerList);
        } else {
            throw new IllegalArgumentException("The explorer does not specify any SUPPORTED explorerType (" + explorerTypeList
                    + ") or other explorer property.\n");
        }
    }

    @Override
    public void inherit(ExplorerConfig inheritedConfig) {
        if (explorerTypeList == null) {
            explorerTypeList = inheritedConfig.getExplorerTypeList();
        } else {
            List<ExplorerType> inheritedExplorerTypeList = inheritedConfig.getExplorerTypeList();
            if (inheritedExplorerTypeList != null) {
                for (ExplorerType explorerType : inheritedExplorerTypeList) {
                    if (!explorerTypeList.contains(explorerType)) {
                        explorerTypeList.add(explorerType);
                    }
                }
            }
        }
        terminationConfig = ConfigUtils.inheritConfig(terminationConfig, inheritedConfig.getTerminationConfig());
    }

}
