package com.auto.example.tests;

import com.auto.example.framework.ExampleTestCase;
import com.auto.framework.utils.ThreadUtils;
import org.junit.Test;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 8:53 am
 * email: mrpjpandey@gmail.com
 */
public class BasicTests extends ExampleTestCase {

    @Test
    public void pingTest() {
        ThreadUtils.sleep(1000);
    }
}
