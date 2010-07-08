/*
 * Copyright (c) 2001-2010 Group JCDecaux.
 * 17 rue Soyer, 92523 Neuilly Cedex, France.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Group JCDecaux ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you
 * entered into with Group JCDecaux.
 */
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
