package com.auto.reqres.tests;

import com.auto.framework.check.Check;
import com.auto.framework.rules.tags.Tags;
import com.auto.reqres.framework.ReqResTestCase;
import com.auto.reqres.model.User;
import org.junit.Test;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 8:53 am
 * email: mrpjpandey@gmail.com
 */
public class BasicTests extends ReqResTestCase {

    @Test
    public void validUser() {
        User user = getServer().getUser(2);
        Check.assertNotNull(user, "User not found");
        Check.assertNotNull(user.getData(), "User data was null");
        Check.assertEquals("janet.weaver@reqres.in", user.getData().getEmail(), "Incorrect email id");
        Check.assertEquals(2, user.getData().getId(), "Id was incorrect");
    }

    @Test
    public void invalidUser() {
        User user = getServer().getUser(23);
        Check.assertNull(user, "User found");
    }

    @Test
    @Tags(value = {"ABC-10","ABD-20"})
    public void adData() {
        User user = getServer().getUser(2);
        Check.assertNotNull(user.getAd(), "Ad was null");
        Check.assertNotNull(user.getAd().getCompany(), "Ad company was null");
        Check.assertEquals("StatusCode Weekly", user.getAd().getCompany(), "Incorrect company name");
    }

    @Test
    @Tags(value = {"ABC-10","ABD-20"})
    public void adDataWithTags() {
        User user = getServer().getUser(2);
        Check.assertNotNull(user.getAd(), "Ad was null");
        Check.assertNotNull(user.getAd().getCompany(), "Ad company was null");
        Check.assertEquals("StatusCode Weekly", user.getAd().getCompany(), "Incorrect company name");
    }
}
