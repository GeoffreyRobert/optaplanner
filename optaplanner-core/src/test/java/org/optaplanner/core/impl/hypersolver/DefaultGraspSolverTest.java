package org.optaplanner.core.impl.hypersolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.hypersolver.HyperSolverConfig;
import org.optaplanner.core.config.hypersolver.switcher.evaluator.EvaluatorConfig;
import org.optaplanner.core.config.hypersolver.switcher.explorer.ExplorerConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.impl.score.DummySimpleScoreEasyScoreCalculator;
import org.optaplanner.core.impl.testdata.domain.TestdataEntity;
import org.optaplanner.core.impl.testdata.domain.TestdataSolution;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.optaplanner.core.impl.testdata.util.PlannerAssert.*;
import static org.optaplanner.core.impl.testdata.util.PlannerTestUtils.TERMINATION_STEP_COUNT_LIMIT;

public class DefaultGraspSolverTest {

    /**
     * Inspiré de org.optaplanner.core.impl.solver.DefaultSolverTest.solve
     */
    @Test
    public void solve() {
        HyperSolverConfig solverConfig = new HyperSolverConfig();
        solverConfig.setSolutionClass(TestdataSolution.class);
        solverConfig.setEntityClassList(Arrays.asList(TestdataEntity.class));

        ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
        scoreDirectorFactoryConfig.setEasyScoreCalculatorClass(DummySimpleScoreEasyScoreCalculator.class);
        solverConfig.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);
        solverConfig.setTerminationConfig(new TerminationConfig().withSecondsSpentLimit(2L));

        solverConfig.setEvaluatorConfig(new EvaluatorConfig());
        solverConfig.setExplorerConfig(new ExplorerConfig());
        solverConfig.getExplorerConfig().setTerminationConfig(new TerminationConfig().withStepCountLimit(TERMINATION_STEP_COUNT_LIMIT));
        SolverConfigContext solverConfigContext = new SolverConfigContext();
        DefaultHyperSolver<TestdataSolution> solver = solverConfig.buildSolver(solverConfigContext);

        TestdataSolution solution = new TestdataSolution("s1");
        solution.setValueList(Arrays.asList(new TestdataValue("v1"), new TestdataValue("v2")));
        solution.setEntityList(Arrays.asList(new TestdataEntity("e1"), new TestdataEntity("e2")));

        solution = solver.solve(solution);
        // solution = solver.getBestSolution();
        assertNotNull(solution);
        assertEquals(true, solution.getScore().isSolutionInitialized());
        assertSame(solution, solver.getBestSolution());
    }
}
