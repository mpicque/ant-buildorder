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
package com.mpi.ant.buildorder.tasks.tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AntBuildOrderResolver - class com.jcdecaux.ant.tasks.DependenciesTreeFactory. date : Jul 8, 2010
 */
public class DependencyTreeFactory {

    private final Map<String, DependencyTree> loadedModules = new HashMap<String, DependencyTree>();

    private DependencyTreeFactory() {
        super();
    }

    public static DependencyTree createDependencyTree(String projectName, File baseDir, String dependenciesFileName)
            throws IOException {
        DependencyTree tree = new DependencyTree(projectName);

        new DependencyTreeFactory().loadDependency(tree, baseDir, dependenciesFileName, new File(dependenciesFileName));

        return tree;
    }

    private void loadDependency(DependencyTree tree, File baseDir, String dependenciesFileName, File dependenciesFile)
            throws IOException {
        List<String> dependencies = readFile(dependenciesFile);

        for (String dependency : dependencies) {
            if (!loadedModules.containsKey(dependency)) {
                DependencyTree leaf = new DependencyTree(dependency);

                File leafDir = new File(baseDir, dependency);
                File leafDependenciesFile = new File(leafDir, dependenciesFileName);
                loadDependency(leaf, baseDir, dependenciesFileName, leafDependenciesFile);

                loadedModules.put(dependency, leaf);
            }
            tree.add(loadedModules.get(dependency));
        }
    }

    private static List<String> readFile(File dependenciesFile) throws IOException {
        List<String> result = new ArrayList<String>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(dependenciesFile));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (line.length() != 0) {
                    result.add(line);
                }
                line = reader.readLine();
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return result;
    }
}
