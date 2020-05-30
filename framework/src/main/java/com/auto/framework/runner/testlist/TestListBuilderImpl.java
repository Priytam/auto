
package com.auto.framework.runner.testlist;

import com.auto.framework.rules.parameterized.ParameterizedTest;
import com.auto.framework.rules.parameterized.TestParametersManager;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestListBuilderImpl implements TestListBuilder {
    public final static String INTERNAL_CLASSES_PREFIX = "com.auto";

    @Override
    public Collection<String> build(String sClassPath, String sClassPrefix) {
        String classesPrefix = StringUtils.isNotBlank(sClassPrefix) ? sClassPrefix : INTERNAL_CLASSES_PREFIX;
        String testsDir = "all";
        Collection<String> colAllTests = new ArrayList<String>();
        Collection<String> colClasses = new TestListBuilderHandler(classesPrefix).getAllClasses(testsDir, sClassPath);
        for (String sClass : colClasses) {
            Collection<String> colTests = getTestsInClass(sClass);
            colAllTests.addAll(colTests);
        }
        return colAllTests;
    }

    @Override
    public Collection<String> build(String sClassPrefix) {
        String classesPrefix = StringUtils.isNotBlank(sClassPrefix) ? sClassPrefix : INTERNAL_CLASSES_PREFIX;
        Set<Method> methods = new TestListBuilderHandler(classesPrefix).getMothods();
        return getTestInMethods(methods.toArray(new Method[0]));
    }

    @Override
    public String getXmlString(Collection<String> colAllTests) {
        StringBuilder rResponse = new StringBuilder();
        addOutput(rResponse, "<tests>\n");
        for (String sTest : colAllTests) {
            addOutput(rResponse, buildTestXML(sTest) + "\n");
        }
        addOutput(rResponse, "</tests>\n");
        return rResponse.toString();
    }

    private void addOutput(StringBuilder rResponse, String s) {
        rResponse.append(s);
    }

    private Collection<String> getTestsInClass(String sClass) {
        Collection<String> colTests = new ArrayList<String>();

        Class clazz;
        try {
            clazz = Class.forName(sClass);
        } catch (Throwable e) {
            return colTests;
        }

        if (!testsAllowed(clazz.getPackage())) {
            return colTests;
        }

        if (!testsAllowed(clazz)) {
            return colTests;
        }
        Method methods[] = clazz.getDeclaredMethods();
        colTests.addAll(getTestInMethods(methods));
        return colTests;
    }

    private Collection<String> getTestInMethods(Method[] methods) {
        Collection<String> tests = Lists.newArrayList();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (!method.isAnnotationPresent(Test.class)) {
                continue;
            }

            if (!testsAllowed(method)) {
                continue;
            }

            Class<?> clazz = method.getDeclaringClass();
            String sTestName = clazz.getName() + "." + method.getName();
            Collection<String> colTestNames = Lists.newArrayList();
            if (isParameterizedTest(clazz, method)) {
                for (int j = 1; j <= generateNumberOfPermutations(clazz, method); j++) {
                    colTestNames.add(sTestName + "[" + j + "]");
                }
            } else {
                colTestNames.add(sTestName);
            }
            tests.addAll(colTestNames);
        }
        return tests;
    }

    private int generateNumberOfPermutations(Class clazz, Method method) {
        try {
            Object tTest = clazz.newInstance();
            TestParametersManager.init(tTest);
            return TestParametersManager.getNumOfIterations();
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean isParameterizedTest(Class clazz, Method method) {
        return matchesAnnotation(clazz, method, ParameterizedTest.class);
    }

    private String buildTestXML(String sTestName) {
        String sOutput = "<test name='" + sTestName + "'>\n";
        sOutput += "</test>";
        return sOutput;
    }

    private boolean testsAllowed(AnnotatedElement eElement) {
        return !eElement.isAnnotationPresent(Ignore.class);
    }

    private boolean matchesAnnotation(Class clazz, Method mMethod, Class<? extends Annotation> annotation) {
        return (mMethod.isAnnotationPresent(annotation) || clazz.isAnnotationPresent(annotation) || clazz.getPackage().isAnnotationPresent(annotation));
    }
}
