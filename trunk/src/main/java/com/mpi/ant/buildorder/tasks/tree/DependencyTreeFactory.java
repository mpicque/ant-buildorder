package com.mpi.ant.buildorder.tasks.tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;

/**
 * AntBuildOrderResolver - class com.jcdecaux.ant.tasks.DependenciesTreeFactory. date : Jul 8, 2010
 */
public class DependencyTreeFactory {

    private final Map<String, DependencyTree> loadedModules = new HashMap<String, DependencyTree>();

    private DependencyTreeFactory() {
        super();
    }

    public static DependencyTree createDependencyTree(String projectName, File baseDir, String dependenciesFileName, String parentProject)
            throws IOException {
        DependencyTree tree = new DependencyTree(projectName);
        File parentDepFile = new File(dependenciesFileName);
        if (!parentDepFile.exists() || !parentDepFile.isFile()) {
            parentDepFile = new File(new File(baseDir, parentProject), dependenciesFileName);
        }

        new DependencyTreeFactory().loadDependency(tree, baseDir, dependenciesFileName, parentDepFile);

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

        if (!dependenciesFile.exists() || !dependenciesFile.isFile()) {
            throw new BuildException("file " + dependenciesFile.getAbsolutePath() + " does not exists");
        }
        
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
