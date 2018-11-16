package org.optaplanner.core.impl.graspsearch;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.graspsearch.GraspSearchPhaseConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.optaplanner.core.impl.testdata.domain.TestdataEntity;
import org.optaplanner.core.impl.testdata.domain.TestdataSolution;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;
import org.optaplanner.core.impl.testdata.domain.immovable.TestdataImmovableEntity;
import org.optaplanner.core.impl.testdata.domain.immovable.TestdataImmovableSolution;
import org.optaplanner.core.impl.testdata.util.PlannerTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.optaplanner.core.impl.testdata.util.PlannerAssert.*;

public class DefaultGraspSearchPhaseTest {

    @Test
    public void solveWithInitializedEntities() {
        SolverFactory<TestdataSolution> solverFactory = PlannerTestUtils.buildSolverFactory(
                TestdataSolution.class, TestdataEntity.class);
        GraspSearchPhaseConfig phaseConfig = new GraspSearchPhaseConfig();
        phaseConfig.setTerminationConfig(new TerminationConfig().withScoreCalculationCountLimit(10L));
        solverFactory.getSolverConfig().setPhaseConfigList(Collections.singletonList(
                phaseConfig));
        Solver<TestdataSolution> solver = solverFactory.buildSolver();

        TestdataSolution solution = new TestdataSolution("s1");
        TestdataValue v1 = new TestdataValue("v1");
        TestdataValue v2 = new TestdataValue("v2");
        TestdataValue v3 = new TestdataValue("v3");
        solution.setValueList(Arrays.asList(v1, v2, v3));
        solution.setEntityList(Arrays.asList(
                new TestdataEntity("e1", v1),
                new TestdataEntity("e2", v2),
                new TestdataEntity("e3", v1)));

        solution = solver.solve(solution);
        assertNotNull(solution);
        TestdataEntity solvedE1 = solution.getEntityList().get(0);
        assertCode("e1", solvedE1);
        assertNotNull(solvedE1.getValue());
        TestdataEntity solvedE2 = solution.getEntityList().get(1);
        assertCode("e2", solvedE2);
        assertNotNull(solvedE2.getValue());
        TestdataEntity solvedE3 = solution.getEntityList().get(2);
        assertCode("e3", solvedE3);
        assertNotNull(solvedE3.getValue());
    }

    @Test
    public void solveWithImmovableEntities() {
        SolverFactory<TestdataImmovableSolution> solverFactory = PlannerTestUtils.buildSolverFactory(
                TestdataImmovableSolution.class, TestdataImmovableEntity.class);
        GraspSearchPhaseConfig phaseConfig = new GraspSearchPhaseConfig();
        phaseConfig.setTerminationConfig(new TerminationConfig().withScoreCalculationCountLimit(10L));
        solverFactory.getSolverConfig().setPhaseConfigList(Collections.singletonList(
                phaseConfig));
        Solver<TestdataImmovableSolution> solver = solverFactory.buildSolver();

        TestdataImmovableSolution solution = new TestdataImmovableSolution("s1");
        TestdataValue v1 = new TestdataValue("v1");
        TestdataValue v2 = new TestdataValue("v2");
        TestdataValue v3 = new TestdataValue("v3");
        solution.setValueList(Arrays.asList(v1, v2, v3));
        solution.setEntityList(Arrays.asList(
                new TestdataImmovableEntity("e1", v1, false, false),
                new TestdataImmovableEntity("e2", v2, true, false),
                new TestdataImmovableEntity("e3", null, false, true)));

        solution = solver.solve(solution);
        assertNotNull(solution);
        TestdataImmovableEntity solvedE1 = solution.getEntityList().get(0);
        assertCode("e1", solvedE1);
        assertNotNull(solvedE1.getValue());
        TestdataImmovableEntity solvedE2 = solution.getEntityList().get(1);
        assertCode("e2", solvedE2);
        assertEquals(v2, solvedE2.getValue());
        TestdataImmovableEntity solvedE3 = solution.getEntityList().get(2);
        assertCode("e3", solvedE3);
        assertEquals(null, solvedE3.getValue());
    }

    @Test
    public void solveWithEmptyEntityList() {
        SolverFactory<TestdataSolution> solverFactory = PlannerTestUtils.buildSolverFactory(
                TestdataSolution.class, TestdataEntity.class);
        GraspSearchPhaseConfig phaseConfig = new GraspSearchPhaseConfig();
        phaseConfig.setTerminationConfig(new TerminationConfig().withScoreCalculationCountLimit(10L));
        solverFactory.getSolverConfig().setPhaseConfigList(Collections.singletonList(
                phaseConfig));
        Solver<TestdataSolution> solver = solverFactory.buildSolver();

        TestdataSolution solution = new TestdataSolution("s1");
        TestdataValue v1 = new TestdataValue("v1");
        TestdataValue v2 = new TestdataValue("v2");
        TestdataValue v3 = new TestdataValue("v3");
        solution.setValueList(Arrays.asList(v1, v2, v3));
        solution.setEntityList(Collections.emptyList());

        solution = solver.solve(solution);
        assertNotNull(solution);
        assertEquals(0, solution.getEntityList().size());
    }

}
