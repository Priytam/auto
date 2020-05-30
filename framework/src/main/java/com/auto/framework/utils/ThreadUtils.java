package com.auto.framework.utils;

import org.apache.log4j.Logger;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class ThreadUtils {
    private static Logger log = Logger.getLogger(ThreadUtils.class);

    public static void sleep(long millis) {
        if (millis <= 0)
            return;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            log.info("sleep() - interrupted");
        }
    }
}
