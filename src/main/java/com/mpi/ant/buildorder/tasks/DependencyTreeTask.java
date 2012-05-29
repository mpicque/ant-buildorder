package com.mpi.ant.buildorder.tasks;

import org.apache.tools.ant.BuildException;

import com.mpi.ant.buildorder.tasks.tree.DependencyTree;

/**
 * AntBuildOrderResolver - class com.jcdecaux.ant.tasks.BuildOrderResolverTask. date : Jul 8, 2010
 */
public class DependencyTreeTask extends AbstractDependencyLoaderTask {

    @Override
    public void execute() throws BuildException {
        checkProperties();

        DependencyTree tree = loadDependencyTree();
        log("Dependency tree :\n" + tree);
    }
}
