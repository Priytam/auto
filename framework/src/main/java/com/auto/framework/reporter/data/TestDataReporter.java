package com.auto.framework.reporter.data;

import com.auto.framework.reporter.TestReporter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestDataReporter {

    private static Map<String, TestDataReporterItem> testData;

    static {
        init();
    }

    public static void init() {
        testData = Maps.newHashMap();
    }

    public static void addData(String sKey, Double sValue) {
        TestReporter.TRACE("Adding data : sKey:" + sKey + " sValue:" + sValue);
        testData.put(sKey, new TestDataReporterItem(sKey, sValue));
    }

    public static void addDataMap(String sKey, Map<String, String> itemMap) {
        try {
            for (String itemKey : itemMap.keySet()) {
                double value = Double.parseDouble(itemMap.get(itemKey));
                if (testData.containsKey(getFullKey(sKey, itemKey))) {
                    testData.get(getFullKey(sKey, itemKey)).updateItem(value);
                } else {
                    addData(getFullKey(sKey, itemKey), value);
                }
            }
        } catch (Exception e) {
            TestReporter.TRACE("Error while reporting " + itemMap + " for " + sKey + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getFullKey(String sKey, String item) {
        return sKey.toLowerCase() + "." + item.toLowerCase();
    }

    public static List<TestDataReporterItem> getTestData() {
        return Lists.newArrayList(testData.values());
    }

}
