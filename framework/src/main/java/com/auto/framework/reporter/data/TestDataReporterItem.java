
package com.auto.framework.reporter.data;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestDataReporterItem {

    private boolean sToBeReported;
    private boolean increaseType;
    private Double sValue;
    private String sKey;
    private int sCount;
    private Double sAvg;
    private Double sMin;
    private Double sMax;

    public TestDataReporterItem(String sKey, Double sValue) {
        this.sKey = sKey.toLowerCase();
        this.sValue = sValue;
        sCount = 1;
        sAvg = sValue;
        sMin = sValue;
        sMax = sValue;
    }

    public String getKey() {
        return sKey;
    }

    public Double getValue() {
        return sValue;
    }

    public boolean shouldBeReported() {
        return sToBeReported;
    }

    public void setToBeReported(boolean val) {
        sToBeReported = val;
    }

    public boolean getIncreaseType() {
        return increaseType;
    }

    public void setIncreaseType(boolean val) {
        increaseType = val;
    }


    public void updateItem(double newValue) {
        sValue = newValue;
        sAvg = ((sAvg * sCount) + newValue) / (sCount + 1);
        sCount++;
        sMax = (sMax < sValue) ? sValue : sMax;
        sMin = (sMin > sValue) ? sValue : sMin;

    }

    public List<TestDataReporterItem> getAllSubDataReporterItems() {
        List<TestDataReporterItem> lstSubData = Lists.newArrayList();
        lstSubData.add(new TestDataReporterItem(sKey + ".avg", sAvg));
        lstSubData.add(new TestDataReporterItem(sKey + ".max", sMax));
        lstSubData.add(new TestDataReporterItem(sKey + ".min", sMin));
        lstSubData.add(this);
        return lstSubData;
    }

}
