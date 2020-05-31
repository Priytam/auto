package com.auto.redis.tests;

import com.auto.framework.check.Check;
import com.auto.redis.framework.RedisTestCase;
import org.junit.Test;

/**
 * User: Priytam Jee Pandey
 * Date: 01/06/20
 * Time: 12:26 am
 * email: priytam.pandey@cleartrip.com
 */
public class BasicTests extends RedisTestCase {

    @Test
    public void testKey() {
        getServer().put("testKey", "testValue");
        Check.assertEquals("testValue", getServer().get("testKey"), "value was not equal");
        Check.assertNull(getServer().get("testKey"), "testKey was present");
    }
}
