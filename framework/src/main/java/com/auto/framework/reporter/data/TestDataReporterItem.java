
package com.auto.framework.reporter.data;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestDataReporterItem {

    private boolean toBeReported;
    private final Object sValue;
    private final String sKey;

    public TestDataReporterItem(String sKey, Object sValue) {
        this.sKey = sKey.toLowerCase();
        this.sValue = sValue;
    }

    public TestDataReporterItem(String sKey, Object sValue, Boolean toBeReported) {
        this.sKey = sKey.toLowerCase();
        this.sValue = sValue;
        this.toBeReported = toBeReported;
    }

    public String getKey() {
        return sKey;
    }

    public Object getValue() {
        return sValue;
    }

    public boolean shouldBeReported() {
        return toBeReported;
    }

    @Override
    public String toString() {
        return "TestDataReporterItem{" +
                "toBeReported=" + toBeReported +
                ", sValue=" + sValue +
                ", sKey='" + sKey + '\'' +
                '}';
    }
}
