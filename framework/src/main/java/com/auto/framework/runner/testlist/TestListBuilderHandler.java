package com.auto.framework.runner.testlist;

import com.google.common.base.Splitter;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import static com.google.common.collect.Lists.newArrayList;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestListBuilderHandler {
    private String[] classesPrefix;

    public TestListBuilderHandler(String classesPrefix) {
        this.classesPrefix = classesPrefix.split(":");
    }

    public Collection<String> getAllClasses(String testsDir, String classPath) {
        String[] urls = classPath.split(":");
        Collection<String> colClassNames = new LinkedList();

        for (int j = 0; j < urls.length; j++) {
            String sJarPath = urls[j];
            Collection colClassNamesThis = null;
            if (sJarPath.endsWith(".jar")) {
                colClassNamesThis = readJar(sJarPath);
            } else {
                colClassNamesThis = readDir(sJarPath, sJarPath);
            }
            if (null != colClassNamesThis) {
                colClassNames.addAll(colClassNamesThis);
            }
        }

        return filterNamesByTestDir(colClassNames, testsDir);
    }

    Collection<String> filterNamesByTestDir(Collection<String> colClassNames, final String testsDir) {
        if ("all".equals(testsDir)) {
            return colClassNames;
        }
        final List<String> testDirsResolved = newArrayList(Splitter.on(',').omitEmptyStrings().split(testsDir));
        return colClassNames.stream().filter(input -> {
            for (String r : testDirsResolved) {
                if (input.contains("." + r + ".")) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }

    private Collection readJar(String sJarPath) {
        Collection colClasses = new LinkedList();
        try {
            JarFile jFile = new JarFile(sJarPath);
            Enumeration jarEntries = jFile.entries();
            while (jarEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) jarEntries.nextElement();
                if (!entry.isDirectory()) {
                    String name = entry.getName().replace('/', '.');
                    if (!isPackageMatch(name)) {
                        continue;
                    }
                    if (name.endsWith(".class")) {
                        name = name.substring(0, name.length() - ".class".length());
                        colClasses.add(name);
                    }
                }
            }
        } catch (Exception e) {
        }
        return colClasses;
    }

    private Collection readDir(String sPath, String sBaseDir) {
        Collection colClasses = new LinkedList();
        File f = new File(sPath);
        if (f.isDirectory()) {
            String[] arrFiles = f.list();
            for (int i = 0; i < arrFiles.length; i++) {
                String sFileName = arrFiles[i];
                if (sFileName.startsWith(".")) {
                    continue;
                }
                Collection colSubDirClasses = readDir(sPath + "/" + sFileName, sBaseDir);
                colClasses.addAll(colSubDirClasses);
            }
        } else if (f.exists()) {
            String name = sPath;
            if (name.endsWith(".class")) {
                name = name.substring(sBaseDir.length() + 1);
                name = name.replace('/', '.');
                if (isPackageMatch(name)) {
                    name = name.substring(0, name.length() - ".class".length());
                    colClasses.add(name);
                }
            }
        }
        return colClasses;
    }

    private boolean isPackageMatch(String name) {
        for (String prefix : classesPrefix) {
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public Set<Method> getMothods() {
        return new Reflections(classesPrefix, new MethodAnnotationsScanner()).getMethodsAnnotatedWith(Test.class);
    }
}
