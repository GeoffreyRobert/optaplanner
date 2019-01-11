package org.optaplanner.core.config.hypersolver.switcher.evaluator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.config.AbstractConfig;
import org.optaplanner.core.config.heuristic.policy.HeuristicConfigPolicy;
import org.optaplanner.core.impl.hypersolver.switcher.evaluator.Evaluator;
import org.optaplanner.core.impl.hypersolver.switcher.evaluator.NoScoreEvaluator;

@XStreamAlias("EvaluatorConfig")
public class EvaluatorConfig extends AbstractConfig<EvaluatorConfig> {

    public EvaluatorConfig() {}

    @Override
    public void inherit(EvaluatorConfig inheritedConfig) {
    }

    public Evaluator buildEvaluator(HeuristicConfigPolicy configPolicy) {
        return new NoScoreEvaluator();
    }

}
