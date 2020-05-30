package com.auto.framework.runner.testlist;

import java.util.Collection;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 5:48 am
 * email: mrpjpandey@gmail.com
 */
public interface TestListBuilder {
    Collection<String> build(String sClassPath, String sClassPrefix);

    Collection<String> build(String sClassPrefix);

    String getXmlString(Collection<String> colAllTests);
}
