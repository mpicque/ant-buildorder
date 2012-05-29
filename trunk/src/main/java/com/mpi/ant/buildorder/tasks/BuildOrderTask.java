package com.mpi.ant.buildorder.tasks;

import java.util.List;

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
        List<String> dependencyList = result.toDependencyList();
        String dependenciesStr = concat(dependencyList, separator);
        getProject().setProperty(property, dependenciesStr);
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

    private String concat(List<String> dependencies, String separator) {
        StringBuilder sb = new StringBuilder();

        log("Computed build order : ");
        for (int i = 0; i < dependencies.size(); i++) {
            log("\t" + (i + 1) + "- " + dependencies.get(i));
            sb.append(dependencies.get(i));
            if (i != dependencies.size() - 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }
}
