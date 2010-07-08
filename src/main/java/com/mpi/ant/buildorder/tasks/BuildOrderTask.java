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
import com.mpi.ant.buildorder.tasks.tree.DependencyTreeResolver;

/**
 * AntBuildOrderResolver - class com.jcdecaux.ant.tasks.BuildOrderResolverTask. date : Jul 8, 2010
 */
public class BuildOrderTask extends AbstractDependencyLoaderTask {
    /**
     * property name which will contains build order.
     */
    private String property;

    /**
     * the separator of dependencies list.
     */
    private String separator;

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public void execute() throws BuildException {
        checkProperties();

        DependencyTree tree = loadDependencyTree();
        DependencyTree result = DependencyTreeResolver.resolve(tree);
        String dependencyList = result.toDependencyList(separator);
        log("Computed build order : " + dependencyList);
        getProject().setProperty(property, dependencyList);
    }

    @Override
    protected void checkProperties() throws BuildException {
        super.checkProperties();
        
        if (property == null || property.trim().length() == 0) {
            throw new BuildException("attribute property must be set");
        }
        
        if (separator == null || separator.length() == 0) {
            throw new BuildException("attribute separator must be set");
        }
    }
}
