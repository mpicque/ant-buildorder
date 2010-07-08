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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AntBuildOrderResolver - class com.jcdecaux.ant.tasks.DependencyTreeResolver. date : Jul 8, 2010
 */
public class DependencyTreeResolver {

    public static DependencyTree resolve(DependencyTree tree) {
        DependencyTree result = tree.clone();
        DependencyTree currentLeaf = result;
        DependencyTree originalLeaf = null;

        while (!result.isLine()) {
            for (DependencyTree leaf : getLeafsExcept(result, currentLeaf)) {
                if ((originalLeaf == null || !originalLeaf.contains(leaf)) && !currentLeaf.containsLeaf(leaf)) {
                    removeAllLeaf(result, leaf);
                    currentLeaf.add(leaf);
                    currentLeaf = leaf;

                    if (originalLeaf == null) {
                        originalLeaf = currentLeaf;
                    }

                    break;
                }
            }
        }

        return result;
    }

    private static List<DependencyTree> getLeafsExcept(DependencyTree result, DependencyTree currentLeaf) {
        List<DependencyTree> leafs = getLeafs(result);
        leafs.remove(currentLeaf);

        leafs = orderLeafByNumber(leafs);

        return leafs;
    }

    private static List<DependencyTree> orderLeafByNumber(List<DependencyTree> leafs) {
        List<DependencyTree> result = new ArrayList<DependencyTree>();
        final Map<DependencyTree, Integer> count = new HashMap<DependencyTree, Integer>();

        for (DependencyTree leaf : leafs) {
            if (!result.contains(leaf)) {
                result.add(leaf);
                count.put(leaf, 0);
            }
            count.put(leaf, count.get(leaf) + 1);
        }
        
        Collections.sort(result, new Comparator<DependencyTree>() {
            public int compare(DependencyTree o1, DependencyTree o2) {
                return count.get(o2).compareTo(count.get(o1));
            }
        });

        return result;
    }

    private static List<DependencyTree> getLeafs(DependencyTree tree) {
        List<DependencyTree> leafs = new ArrayList<DependencyTree>();

        for (DependencyTree leaf : tree.getLeafs()) {
            if (leaf.isLeaf()) {
                leafs.add(leaf);
            } else {
                leafs.addAll(getLeafs(leaf));
            }
        }

        return leafs;
    }

    private static void removeAllLeaf(DependencyTree tree, DependencyTree leaf) {
        for (DependencyTree currentLeaf : tree.getLeafs()) {
            removeAllLeaf(currentLeaf, leaf);
        }

        tree.removeLeaf(leaf);
    }
}
