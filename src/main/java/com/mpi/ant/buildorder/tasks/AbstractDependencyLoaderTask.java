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
    
    /**
     * parent project, used to initialize the first dependency file if not found in the exec. directory
     */
    protected String parentProject = "builder";
    
    public void setDependenciesFileName(String dependenciesFileName) {
        this.dependenciesFileName = dependenciesFileName;
    }

    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }
    
    public void setParentProject(String parentProject) {
    	this.parentProject = parentProject;
    }

    public DependencyTree loadDependencyTree() throws BuildException {
        try {
            return DependencyTreeFactory.createDependencyTree(getProject().getName(), baseDir, dependenciesFileName, 
                    parentProject);
        } catch (IOException e) {
            throw new BuildException("Unable to build dependency tree", e);
        }
    }

    protected void checkProperties() throws BuildException {
        if (dependenciesFileName == null || dependenciesFileName.trim().length() == 0) {
            throw new BuildException("attribute dependenciesfilename must be set");
        }

        if (baseDir == null) {
            throw new BuildException("attribute basedir must be set");
        }

        if (!baseDir.exists() || !baseDir.isDirectory()) {
            throw new BuildException("file " + baseDir.getAbsolutePath() + " does not exists");
        }
    }

}