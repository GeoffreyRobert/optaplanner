package org.optaplanner.core.impl.hypersolver;

import java.io.IOException;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.solver.SolverConfig;

import static org.junit.Assert.*;

public class XStreamXmlHyperSolverFactoryTest {

    @Test
    public void configFileRemainsSameAfterReadWrite() throws IOException {
        String solverConfigResource = "testdataHyperSolverConfigXStream.xml";
        String originalXml = IOUtils.toString(getClass().getResourceAsStream(solverConfigResource), "UTF-8");
        InputStream originalConfigInputStream = getClass().getResourceAsStream(solverConfigResource);
        XStreamXmlHyperSolverFactory hyperSolverFactory = new XStreamXmlHyperSolverFactory().configure(originalConfigInputStream);
        hyperSolverFactory.getXStream().setMode(XStream.NO_REFERENCES);
        SolverConfig solverConfig = hyperSolverFactory.getSolverConfig();
        SolverConfigContext configContext = new SolverConfigContext(getClass().getClassLoader());
        solverConfig.buildSolver(configContext);
        String savedXml = hyperSolverFactory.getXStream().toXML(solverConfig);
        assertEquals(originalXml.trim(), savedXml.trim());
        originalConfigInputStream.close();
    }

}
