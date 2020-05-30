package com.auto.framework.env;

import org.apache.commons.lang3.StringUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public enum RegressionEnvironment {
    REGRESSION, LOCAL, NIGHTLY;

    public static boolean isRegression() {
        return StringUtils.isNotBlank(getValue(REGRESSION.name()));
    }

    public static String getValue(String key) {
        if (null != System.getProperty(key)) {
            return System.getProperty(key);
        }

        if (null != System.getenv(key)) {
            return System.getenv(key);
        }
        return null;
    }
}
