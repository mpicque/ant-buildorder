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

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

/**
 * AntBuildOrderResolver - class com.jcdecaux.ant.tasks.DependencyTree. date : Jul 8, 2010
 */
public class DependencyTree implements Cloneable {
    private List<DependencyTree> leafs = new ArrayList<DependencyTree>();

    private String name;

    public DependencyTree(String name) {
        this.name = name;
    }

    public boolean isLeaf() {
        return leafs.isEmpty();
    }

    public void add(DependencyTree subTree) {
        leafs.add(subTree);
    }

    public List<DependencyTree> getLeafs() {
        return leafs;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int level = 1;

        sb.append(name);
        sb.append('\n');

        sb.append(leafsToString(leafs, level));

        return sb.toString();
    }

    private static String leafsToString(List<DependencyTree> leafs, int level) {
        StringBuilder sb = new StringBuilder();

        for (DependencyTree entry : leafs) {
            for (int i = 0; i < level - 1; i++) {
                sb.append("|  ");
            }
            sb.append("|- ");
            sb.append(entry.name);
            sb.append('\n');
            sb.append(leafsToString(entry.leafs, level + 1));
        }

        return sb.toString();
    }

    @Override
    protected DependencyTree clone() {
        DependencyTree clone = new DependencyTree(name);

        for (DependencyTree entry : leafs) {
            clone.add(entry.clone());
        }

        return clone;
    }

    public String toDependencyList(String separator) throws BuildException {
        if (!isLine()) {
            throw new BuildException("Invalid build dependencies");
        }
        StringBuilder sb = new StringBuilder();

        sb.append(leafs.get(0).name);
        if (!leafs.get(0).isLeaf()) {
            sb.append(separator);
            sb.append(leafs.get(0).toDependencyList(separator));
        }

        return sb.toString();
    }

    public boolean isLine() {
        return isLeaf() || (leafs.size() == 1 && leafs.get(0).isLine());
    }

    public boolean containsLeaf(DependencyTree leaf) {
        return leafs.contains(leaf.name);
    }

    public boolean isFlat() {
        boolean result = true;

        for (DependencyTree leaf : getLeafs()) {
            result &= leaf.isLeaf();
        }

        return result;
    }

    public void removeLeaf(DependencyTree leaf) {
        leafs.remove(leaf);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DependencyTree && name.equals(((DependencyTree) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean contains(DependencyTree leaf) {
        boolean result = false;

        if (this.equals(leaf)) {
            result = true;
        } else {
            for (DependencyTree currentLeaf : leafs) {
                if (currentLeaf.contains(leaf)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
