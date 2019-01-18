package org.optaplanner.core.impl.hyperheuristic;

import java.io.IOException;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.impl.solver.XStreamXmlSolverFactory;

import static org.junit.Assert.*;

public class XStreamXmlHyperSolverFactoryTest {

    @Test
    public void configFileRemainsSameAfterReadWrite() throws IOException {
        String solverConfigResource = "testdataHyperHeuristicConfigXStream.xml";
        String originalXml = IOUtils.toString(getClass().getResourceAsStream(solverConfigResource), "UTF-8");
        InputStream originalConfigInputStream = getClass().getResourceAsStream(solverConfigResource);
        XStreamXmlSolverFactory solverFactory = new XStreamXmlSolverFactory().configure(originalConfigInputStream);
        solverFactory.getXStream().setMode(XStream.NO_REFERENCES);
        SolverConfig solverConfig = solverFactory.getSolverConfig();
        SolverConfigContext configContext = new SolverConfigContext(getClass().getClassLoader());
        solverConfig.buildSolver(configContext);
        String savedXml = solverFactory.getXStream().toXML(solverConfig);
        assertEquals(originalXml.trim(), savedXml.trim());
        originalConfigInputStream.close();
    }

}
