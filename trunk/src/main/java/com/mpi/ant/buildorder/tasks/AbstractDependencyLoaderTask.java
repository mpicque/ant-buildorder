package com.mpi.ant.buildorder.tasks;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.mpi.ant.buildorder.tasks.tree.DependencyTree;
import com.mpi.ant.buildorder.tasks.tree.DependencyTreeFactory;

/**
 * AntBuildOrderResolver - class com.jcdecaux.ant.tasks.AbstractDependencyLoaderTask. date : Jul 8, 2010
 */
public abstract class AbstractDependencyLoaderTask extends Task {

    /**
     * name of depedencies file.
     */
    protected String dependenciesFileName;

    /**
     * the base directory of all projects.
     */
    protected File baseDir;

    public void setDependenciesFileName(String dependenciesFileName) {
        this.dependenciesFileName = dependenciesFileName;
    }

    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }

    public DependencyTree loadDependencyTree() throws BuildException {
        checkProperties();

        DependencyTree tree;
        try {
            tree = DependencyTreeFactory.createDependencyTree(getProject().getName(), baseDir, dependenciesFileName);
        } catch (IOException e) {
            throw new BuildException("Unable to build dependency tree", e);
        }

        return tree;
    }

    protected void checkProperties() throws BuildException {
        if (dependenciesFileName == null || dependenciesFileName.trim().length() == 0) {
            throw new BuildException("attribute dependenciesfilename must be set");
        }

        File dependenciesFile = new File(dependenciesFileName);
        if (!dependenciesFile.exists() || !dependenciesFile.isFile()) {
            throw new BuildException("file " + dependenciesFileName + " does not exists");
        }

        if (baseDir == null) {
            throw new BuildException("attribute basrdir must be set");
        }

        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new BuildException("file " + baseDir.getAbsolutePath() + " does not exists");
        }
    }

}