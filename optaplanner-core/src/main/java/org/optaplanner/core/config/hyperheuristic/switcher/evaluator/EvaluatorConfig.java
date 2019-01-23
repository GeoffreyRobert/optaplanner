package org.optaplanner.core.config.hyperheuristic.switcher.evaluator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.impl.hyperheuristic.switcher.evaluator.evaluationfunction.EvaluationFunction;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.hyperheuristic.switcher.evaluator.Evaluator;
import org.optaplanner.core.impl.hyperheuristic.switcher.evaluator.NoScoreEvaluator;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("EvaluatorConfig")
public class EvaluatorConfig extends AbstractConfig<EvaluatorConfig> {

    @XStreamAlias("evaluatorType")
    private List<EvaluatorType> evaluatorTypeList = null;

    // TODO maybe add some sort of external function
    // private EvaluationFunction evaluationFunction = null;

    // ************************************************************************
    // Simple getters/setters
    // ************************************************************************

    public List<EvaluatorType> getEvaluatorTypeList() {
        return evaluatorTypeList;
    }

    public void setEvaluatorTypeList(List<EvaluatorType> evaluatorTypeList) {
        this.evaluatorTypeList = evaluatorTypeList;
    }

    // ************************************************************************
    // With methods
    // ************************************************************************


    // ************************************************************************
    // Builder methods
    // ************************************************************************

    public Evaluator buildEvaluator(HeuristicConfigPolicy configPolicy) {
        List<Evaluator> evaluatorList = new ArrayList<>();
        if (evaluatorTypeList != null && evaluatorTypeList.contains(EvaluatorType.NO_SCORE)) {
            NoScoreEvaluator evaluator = new NoScoreEvaluator();
            evaluatorList.add(evaluator);
        }
        if (evaluatorList.size() == 1) {
            return evaluatorList.get(0);
        } else if (evaluatorList.size() > 1) {
            throw new IllegalArgumentException("The evaluator does not yet support composition.\n");
            // TODO Implement CompositeEvaluator
            // return new CompositeEvaluator(evaluatorList);
        } else {
            throw new IllegalArgumentException("The evaluator does not specify any SUPPORTED evaluatorType (" + evaluatorTypeList
                    + ") or other evaluator property.\n");
        }
    }

    @Override
    public void inherit(EvaluatorConfig inheritedConfig) {
        if (evaluatorTypeList == null) {
            evaluatorTypeList = inheritedConfig.getEvaluatorTypeList();
        } else {
            List<EvaluatorType> inheritedEvaluatorTypeList = inheritedConfig.getEvaluatorTypeList();
            if (inheritedEvaluatorTypeList != null) {
                for (EvaluatorType evaluatorType : inheritedEvaluatorTypeList) {
                    if (!evaluatorTypeList.contains(evaluatorType)) {
                        evaluatorTypeList.add(evaluatorType);
                    }
                }
            }
        }
    }
}
