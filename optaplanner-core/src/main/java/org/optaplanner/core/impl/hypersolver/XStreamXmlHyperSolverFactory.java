package org.optaplanner.core.impl.hypersolver;

import java.io.*;
import java.util.Objects;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import org.optaplanner.core.config.SolverConfigContext;
import org.optaplanner.core.config.hypersolver.HyperSolverConfig;
import org.optaplanner.core.impl.solver.AbstractSolverFactory;

public class XStreamXmlHyperSolverFactory<Solution_> extends AbstractSolverFactory<Solution_> {

    public static XStream buildXStream() {
        XStream xStream = new XStream();
        xStream.setMode(XStream.ID_REFERENCES);
        xStream.aliasSystemAttribute("xStreamId", "id");
        xStream.aliasSystemAttribute("xStreamRef", "reference");
        xStream.processAnnotations(HyperSolverConfig.class);
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypesByRegExp(new String[]{"org\\.optaplanner\\.\\w+\\.config\\..*"});
        return xStream;
    }

    // ************************************************************************
    // Non-static fields and methods
    // ************************************************************************

    protected XStream xStream;

    public XStreamXmlHyperSolverFactory() {
        this(new SolverConfigContext());
    }

    /**
     * @param solverConfigContext never null
     */
    public XStreamXmlHyperSolverFactory(SolverConfigContext solverConfigContext) {
        super(solverConfigContext);
        xStream = buildXStream();
        ClassLoader actualClassLoader = solverConfigContext.determineActualClassLoader();
        xStream.setClassLoader(actualClassLoader);
    }

    /**
     * @param xStreamAnnotations never null
     * @see XStream#processAnnotations(Class[])
     */
    public void addXStreamAnnotations(Class<?>... xStreamAnnotations) {
        xStream.processAnnotations(xStreamAnnotations);
        xStream.allowTypes(xStreamAnnotations);
    }

    public XStream getXStream() {
        return xStream;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    /**
     * @param solverConfigResource never null, a classpath resource
     * as defined by {@link ClassLoader#getResource(String)}
     * @return this
     */
    public XStreamXmlHyperSolverFactory<Solution_> configure(String solverConfigResource) {
        ClassLoader actualClassLoader = solverConfigContext.determineActualClassLoader();
        try (InputStream in = actualClassLoader.getResourceAsStream(solverConfigResource)) {
            if (in == null) {
                String errorMessage = "The solverConfigResource (" + solverConfigResource
                        + ") does not exist as a classpath resource in the classLoader (" + actualClassLoader + ").";
                if (solverConfigResource.startsWith("/")) {
                    errorMessage += "\nAs from 6.1, a classpath resource should not start with a slash (/)."
                            + " A solverConfigResource now adheres to ClassLoader.getResource(String)."
                            + " Remove the leading slash from the solverConfigResource if you're upgrading from 6.0.";
                }
                throw new IllegalArgumentException(errorMessage);
            }
            return configure(in);
        } catch (ConversionException e) {
            String lineNumber = e.get("line number");
            throw new IllegalArgumentException("Unmarshalling of solverConfigResource (" + solverConfigResource
                    + ") fails on line number (" + lineNumber + ")."
                    + (Objects.equals(e.get("required-type"), "java.lang.Class")
                    ? "\n  Maybe the classname on line number (" + lineNumber + ") is surrounded by whitespace, which is invalid."
                    : ""), e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Reading the solverConfigResource (" + solverConfigResource + ") failed.", e);
        }
    }

    public XStreamXmlHyperSolverFactory<Solution_> configure(File solverConfigFile) {
        try (InputStream in = new FileInputStream(solverConfigFile)) {
            return configure(in);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("The solverConfigFile (" + solverConfigFile + ") was not found.", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Reading the solverConfigFile (" + solverConfigFile + ") failed.", e);
        }
    }

    public XStreamXmlHyperSolverFactory<Solution_> configure(InputStream in) {
        try (Reader reader = new InputStreamReader(in, "UTF-8")) {
            return configure(reader);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("This vm does not support UTF-8 encoding.", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Reading failed.", e);
        }
    }

    public XStreamXmlHyperSolverFactory<Solution_> configure(Reader reader) {
        solverConfig = (HyperSolverConfig) xStream.fromXML(reader);
        return this;
    }

}
