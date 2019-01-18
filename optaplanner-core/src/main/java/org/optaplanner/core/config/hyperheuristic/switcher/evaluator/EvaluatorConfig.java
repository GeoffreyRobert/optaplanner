package org.optaplanner.core.config.hyperheuristic.switcher.evaluator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.impl.hyperheuristic.switcher.evaluator.evaluationfunction.EvaluationFunction;
import org.optaplanner.core.config.util.ConfigUtils;
import org.optaplanner.core.impl.hyperheuristic.switcher.evaluator.Evaluator;
import org.optaplanner.core.impl.hyperheuristic.switcher.evaluator.NoScoreEvaluator;

import java.util.List;

@XStreamAlias("EvaluatorConfig")
public class EvaluatorConfig extends AbstractConfig<EvaluatorConfig> {

    @XStreamAlias("evaluatorType")
    private List<EvaluatorType> evaluatorTypeList = null;

    private EvaluationFunction evaluationFunction = null;

    // ************************************************************************
    // Simple getters/setters
    // ************************************************************************

    public List<EvaluatorType> getEvaluatorTypeList() {
        return evaluatorTypeList;
    }

    public void setEvaluatorTypeList(List<EvaluatorType> evaluatorTypeList) {
        this.evaluatorTypeList = evaluatorTypeList;
    }

    public EvaluationFunction getEvaluationFunction() {
        return evaluationFunction;
    }

    public void setEvaluationFunction(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    // ************************************************************************
    // With methods
    // ************************************************************************

    public EvaluatorConfig withEvaluationFunction(EvaluationFunction evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
        return this;
    }

    // ************************************************************************
    // Builder methods
    // ************************************************************************

    public Evaluator buildEvaluator(HeuristicConfigPolicy configPolicy) {
        return new NoScoreEvaluator();
    }

    @Override
    public void inherit(EvaluatorConfig inheritedConfig) {
        evaluationFunction = ConfigUtils.inheritOverwritableProperty(evaluationFunction,
                inheritedConfig.getEvaluationFunction());
    }
}
