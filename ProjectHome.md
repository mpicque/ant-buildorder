This project aims to automatically manage multiproject architecture with Ant.

This project did not aim to manage the dependencies jars.

Example :

```
<project name="Example" default="example">
    <target name="init">
        <taskdef resource="com/mpi/ant/buildorder/antbuildorder.properties">
            <classpath>
                <pathelement location="ant-buildorder-0.1-SNAPSHOT.jar" />
            </classpath>
        </taskdef>
    </target>

    <target name="example" depends="init">
        <buildOrder property="build-order" dependenciesfilename="librairies.properties" separator="-" basedir=".." />
        <dependencyTree dependenciesfilename="librairies.properties" basedir=".." />
    </target>
</project>
```

Explanations :
  * result of buildOrder will be set into property "build-order", each items is separated by specified separator
  * all projects must have a file named "librairies.properties" containing dependencies (one by line)
  * "base.dir" is the directory containing all projects
  * buildOrder task set result into specified property and prompt computed build order
  * dependencyTree task prompt dependency tree created with librairies.properties file

For a complete example, go to http://code.google.com/p/ant-buildorder/source/browse/trunk/example.