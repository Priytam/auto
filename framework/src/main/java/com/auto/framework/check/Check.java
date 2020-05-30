package com.auto.framework.check;

import com.auto.framework.reporter.TestReporter;
import com.auto.framework.utils.ThreadUtils;
import com.auto.framework.utils.regex.RegexParser;
import com.google.common.collect.Lists;
import junit.framework.ComparisonFailure;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class Check {
    private static abstract class ErrorMessageGenerator {
        public abstract String generateMessage();
    }

    private static class StringMessageGenerator extends ErrorMessageGenerator {
        private String m_sMessage;

        public StringMessageGenerator(String errorMessage) {
            m_sMessage = errorMessage;
        }

        @Override
        public String generateMessage() {
            return m_sMessage;
        }

    }

    public static <T extends Object> boolean checkBusyWait(Predicate<? super T> predicate, Supplier<T> subject, int timesToCheck, int intervalSeconds) {
        if (timesToCheck <= 0) {
            throw new IllegalArgumentException("timesToCheck less than 1 " + timesToCheck);
        }
        for (int counter = 0; counter < timesToCheck; ++counter) {
            if (predicate.test(subject.get())) {
                return true;
            }
            ThreadUtils.sleep(intervalSeconds * 1000);
        }
        return false;
    }

    public static <T extends Object> boolean checkBusyWaitTrueForXTime(Predicate<? super T> predicate, Supplier<T> subject, int timesToCheck, int intervalSeconds) {
        if (timesToCheck <= 0) {
            throw new IllegalArgumentException("timesToCheck less than 1 " + timesToCheck);
        }
        for (int counter = 0; counter < timesToCheck; ++counter) {
            if (!predicate.test(subject.get())) {
                return false;
            }
            ThreadUtils.sleep(intervalSeconds * 1000);
        }
        return true;
    }

    public static <T extends Object> void assertBusyWait(Predicate<? super T> predicate, Supplier<T> subject, int timesToCheck, int intervalSeconds, String errorMsg) {
        if (StringUtils.isEmpty(errorMsg)) {
            assertBusyWait(predicate, subject, timesToCheck, intervalSeconds);
        } else {
            assertBusyWait(predicate, subject, timesToCheck, intervalSeconds, new StringMessageGenerator(errorMsg));
        }
    }

    private static <T extends Object> void assertBusyWait(Predicate<? super T> predicate, Supplier<T> subject, int timesToCheck, int intervalSeconds, ErrorMessageGenerator errorMsg) {
        if (!checkBusyWait(predicate, subject, timesToCheck, intervalSeconds)) {
            TestReporter.FATAL(errorMsg.generateMessage());
        }
    }

    public static <T extends Object> void assertBusyWaitTrueForXTime(Predicate<? super T> predicate, Supplier<T> subject, int timesToCheck, int intervalSeconds, String errorMsg) {
        if (!checkBusyWaitTrueForXTime(predicate, subject, timesToCheck, intervalSeconds)) {
            TestReporter.FATAL(new StringMessageGenerator(errorMsg).generateMessage());
        }
    }

    public static <T extends Object> void assertBusyWait(final Predicate<? super T> predicate, final Supplier<T> subject, int timesToCheck) {
        assertBusyWait(predicate, subject, timesToCheck, 1);
    }

    public static <T extends Object> void assertBusyWait(final Predicate<? super T> predicate, final Supplier<T> subject, int timesToCheck, int intervalSeconds) {
        assertBusyWait(predicate, subject, timesToCheck, intervalSeconds, new ErrorMessageGenerator() {
            @Override
            public String generateMessage() {
                return "Timeout to evaluate " + predicate + " on " + subject;
            }
        });
    }

    public static <T extends Object> boolean checkBusyWaitXTime(Predicate<? super T> predicate, int timesToCheck) {
        return checkBusyWait(predicate, null, timesToCheck, 1);
    }

    public static <T extends Object> boolean checkBusyWait10(Predicate<? super T> predicate) {
        return checkBusyWaitXTime(predicate, 10);
    }

    public static void assertTrue(boolean bValue, String... errorMessage) {
        assertEquals(true, bValue, errorMessage);
    }

    public static void assertFalse(boolean bValue, String... errorMessage) {
        assertEquals(false, bValue, errorMessage);
    }

    public static void assertMatches(String sPattern, String sValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!matches(sPattern, sValue)) {
            TestReporter.FATAL(new ComparisonFailure(sFinalMessage + ": pattern=" + sPattern + ", value= " + sValue, sPattern, sValue));
        }
    }

    private static boolean matches(String sPattern, String sValue) {
        sPattern = safeToString(sPattern);
        sValue = safeToString(sValue);
        return RegexParser.parse(sPattern, sValue).isMatch();
    }

    public static void assertEquals(Object oExpected, Object oActual, String... errorMessage) {
        String expected = safeToString(oExpected);
        String actual = safeToString(oActual);
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!equals(expected, actual)) {
            TestReporter.FATAL(new ComparisonFailure(sFinalMessage + " failed", expected, actual));
        }
    }

    public static void assertEqualsWithBusyWait(Object oExpected, Object oActual, String... errorMessage) {
        final String expected = safeToString(oExpected);
        final String actual = safeToString(oActual);

        String sFinalMessage = updateDefaultMessage(errorMessage);

        boolean isTrue = checkBusyWait(Subject -> equals(expected, actual), () -> "", 1, 300);

        if (!isTrue) {
            TestReporter.FATAL(new ComparisonFailure(sFinalMessage + " failed", expected, actual));
        }
    }

    public static void assertEqualsIgnoreCase(String expected, String actual, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);

        if (!equalsIgnoreCase(expected, actual)) {
            TestReporter.FATAL(new ComparisonFailure(sFinalMessage + " failed", expected, actual));
        }
    }

    public static boolean equals(String obj1, String obj2) {
        if (null == obj1 || "".equals(obj1)) {
            return (obj2 == null || "".equals(obj2));
        }
        return obj1.equals(obj2);
    }

    public static boolean equalsIgnoreCase(String obj1, String obj2) {
        if (null == obj1 || "".equals(obj1)) {
            return (obj2 == null || "".equals(obj2));
        }
        return obj1.equalsIgnoreCase(obj2);
    }

    public static void assertPathEquals(String expected, String actual, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        String e = normalizePath(expected);
        String a = normalizePath(actual);
        if (!equals(e, a)) {
            TestReporter.FATAL(new ComparisonFailure(sFinalMessage + " failed", e, a));
        }
    }

    protected static String normalizePath(String path) {
        return path.replaceAll("/+", "/").replaceAll("/$", "").replaceAll("^/nfs/([^/]+)/", "/nfs/site/");
    }

    public static void assertNotEquals(Object notExpected, Object actual, String... errorMessage) {
        String sNotExpected = safeToString(notExpected);
        String sActual = safeToString(actual);

        String sFinalMessage = updateDefaultMessage(errorMessage);

        if (equals(sNotExpected, sActual)) {
            TestReporter.FATAL(sFinalMessage + ": Should not be:" + sNotExpected);
        }
    }

    public static void checkEquals(Object oExpected, Object oActual, String... errorMessage) {
        TestReporter.TRACE("expected = " + oExpected + ", actual = " + oActual);
        String expected = safeToString(oExpected);
        String actual = safeToString(oActual);
        String sFinalMessage = updateDefaultMessage(errorMessage);

        if (!expected.equals(actual)) {
            TestReporter.FAIL(sFinalMessage += " failed (expected:'" + expected + "', actual:'" + actual + "')");
        } else {
            TestReporter.PASS(sFinalMessage + ": " + expected + " equals " + actual);
        }
    }

    public static String safeToString(Object o) {
        if (null == o) {
            return "";
        }
        return o.toString();
    }

    public static void checkContains(String input, String shouldContain, String... sErrorMessage) {
        String errorMessage = updateDefaultMessage(sErrorMessage);

        if (!input.contains(shouldContain)) {
            TestReporter.FAIL(errorMessage += " failed (the input: " + input + " doesn't contain : " + shouldContain + ")");
        } else {
            TestReporter.PASS(input + " contains " + shouldContain);
        }
    }

    public static void checkBetween(long lMin, long lMax, long lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!isBetween(lMin, lMax, lValue)) {
            TestReporter.FAIL(sFinalMessage + ": min=" + lMin + ", max=" + lMax + ", value= " + lValue);
        } else {
            TestReporter.PASS(sFinalMessage + ": min=" + lMin + ", max=" + lMax + ", value= " + lValue);
        }
    }

    public static void checkLessThanOrEqual(long lMax, long lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (lValue <= lMax) {
            TestReporter.PASS(" max=" + lMax + ", value= " + lValue);

        } else {
            TestReporter.FAIL(sFinalMessage + ": max=" + lMax + ", value= " + lValue);
        }
    }

    public static void assertBetween(long lMin, long lMax, long lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!isBetween(lMin, lMax, lValue)) {
            TestReporter.FATAL(sFinalMessage + ": min=" + lMin + ", max=" + lMax + ", value= " + lValue);
        }
    }

    public static void assertNotBetween(int lMin, int lMax, int lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (isBetween(lMin, lMax, lValue)) {
            TestReporter.FATAL(sFinalMessage + ": min=" + lMin + ", max=" + lMax + ", value= " + lValue);
        }
    }

    public static boolean isBetween(long lMin, long lMax, long lValue) {
        return (lValue >= lMin) && (lValue <= lMax);
    }

    public static void assertBetween(Double lMin, Double lMax, Double lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!isBetween(lMin, lMax, lValue)) {
            TestReporter.FATAL(sFinalMessage + ": min=" + lMin + ", max=" + lMax + ", value= " + lValue);
        }
    }

    public static void assertLessThan(double excpected, double lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!isLessThan(excpected, lValue)) {
            TestReporter.FATAL(sFinalMessage + ": should be less than= " + excpected + ", value= " + lValue);
        }
    }

    public static void assertGreaterThan(double excpected, double lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!isGreaterThan(excpected, lValue)) {
            TestReporter.FATAL(sFinalMessage + ": should be less than= " + excpected + ", value= " + lValue);
        }
    }

    public static void assertBetweenNotEqualTo(Date lMin, Date lMax, Date lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!(isLessThan(lMin, lValue) && isLessThan(lValue, lMax))) {
            TestReporter.FATAL(sFinalMessage + ": min=" + lMin + ", max=" + lMax + ", value= " + lValue);
        }
    }

    public static void assertBetween(Date lMin, Date lMax, Date lValue, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!isBetween(lMin.getTime(), lMax.getTime(), lValue.getTime())) {
            TestReporter.FATAL(sFinalMessage + ": min=" + lMin + ", max=" + lMax + ", value= " + lValue);
        }
    }

    private static String updateDefaultMessage(String[] errorMessage) {
        return String.join(" ", errorMessage);
    }

    public static void assertLessThan(Date lMin, Date lMax, String... errorMessage) {
        String sFinalMessage = updateDefaultMessage(errorMessage);
        if (!(isLessThan(lMin, lMax))) {
            TestReporter.FATAL(sFinalMessage + ": min=" + lMin + ", max=" + lMax);
        }
    }

    private static boolean isLessThan(Date lMin, Date lMax) {
        return (lMax.compareTo(lMin) > 0);
    }

    private static boolean isBetween(Double lMin, Double lMax, Double lValue) {
        return (lValue >= lMin) && (lValue <= lMax);
    }

    private static boolean isLessThan(Double excpected, Double value) {
        return (value <= excpected);
    }

    private static boolean isGreaterThan(Double excpected, Double value) {
        return (excpected <= value);
    }

    public static void checkTrue(boolean bValue, String... errorMessage) {
        checkEquals(true, bValue, errorMessage);
    }

    public static void checkFalse(boolean bValue, String... errorMessage) {
        checkEquals(false, bValue, errorMessage);
    }

    public static void assertContainsIgnoreSpaces(String input, String shouldContain) {
        input = input.replaceAll(" ", "");
        shouldContain = shouldContain.replaceAll(" ", "");
        assertTrue(input.contains(shouldContain), input, " does not contain ", shouldContain);
    }

    public static void assertContains(String input, String shouldContain, String sErrorMessage) {
        if (!input.contains(shouldContain)) {
            TestReporter.FATAL("assertContains failed: '" + input + "' does not contains '" + shouldContain + "'. additional error message " + sErrorMessage);
        }
    }

    public static void assertContains(String input, String shouldContain) {
        assertContains(input, shouldContain, " does not contain " + shouldContain);
    }

    public static void assertNotContains(String input, String shouldNotContain) {
        assertFalse(input.contains(shouldNotContain), input, " contain ", shouldNotContain);
    }

    public static void assertContainsOneOf(String input, String... shouldContain) {
        for (String s : shouldContain) {
            if (input.contains(s)) {
                return;
            }
        }
        TestReporter.FATAL("assertContainsOneOf failed: '" + input + "' does not contains '" + Arrays.toString(shouldContain));
    }

    public static void assertContainsAllOf(String input, String... shouldContain) {
        for (String s : shouldContain) {
            if (!input.contains(s)) {
                TestReporter.FATAL("assertContainsAllOf failed: '" + input + "' does not contains '" + s);
            }

        }
        return;
    }

    public static void assertNotNull(Object o, String... errorMessage) {
        assertNotEquals(null, o, errorMessage);
    }

    public static void assertNull(Object o, String... errorMessage) {
        assertEquals(null, o, errorMessage);
    }

    public static void assertMapEquals(String expected, Object oActual, String paramsDelimiter) {
        String actual = safeToString(oActual);

        String errorMessage = "Comparing output";

        if (!mapEquals(expected, actual, paramsDelimiter)) {
            TestReporter.FATAL(new ComparisonFailure(errorMessage + " failed", expected, actual));
        }
    }

    public static boolean mapEquals(String expected, String actual, String paramsDelimiter) {
        /*try {
            Map<String, Object> mp1 = StringParser.stringToMap(expected, paramsDelimiter, "=").getMap();
            Map<String, Object> mp2 = StringParser.stringToMap(actual, paramsDelimiter, "=").getMap();
            return mp1.equals(mp2);
        } catch (ParsingException e) {
            TestReporter.FATAL("Fail to parse map: " + e.getMessage());
            return false;
        }*/
        return false;
    }

    public static void assertCollectionsEqualIgnoreOrder(String expected, Object oActual, String paramsDelimiter) {
        String actual = safeToString(oActual);
        Collection<String> lstActual = Lists.newArrayList(actual.split(" "));
        Collection<String> lstExpected = Lists.newArrayList(expected.split(" "));

        String errorMessage = "Comparing output";

        if (!lstExpected.containsAll(lstActual) || !lstActual.containsAll(lstExpected)) {
            TestReporter.FATAL(new ComparisonFailure(errorMessage + " failed", expected, actual));
        }
    }

    public static void assertDoubleEquals(double expected, double actual, double precision, String... errorMessage) {
        String sFinalMessage = String.join(" ", errorMessage);

        double expectedNormalized = Math.round(expected / precision);
        double actualNormalized = Math.round(actual / precision);

        if (expectedNormalized != actualNormalized) {
            TestReporter.FATAL(new ComparisonFailure(sFinalMessage + " failed", Double.toString(expected), Double.toString(actual)));
        }
    }
}
