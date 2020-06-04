package com.auto.framework.operation.http;

import com.auto.framework.AbstractTestCase;
import com.auto.framework.iface.ITestComponent;
import com.auto.framework.reporter.TestReporter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 03/06/20
 * Time: 3:52 pm
 * email: priytam.pandey@cleartrip.com
 */
public class AbstractHttpOperationTest {

    private AbstractHttpOperation op;
    private AbstractHttpOperation failOp;

    @Before
    public void prepare() {
        AbstractTestCase tTest = new AbstractTestCase("test") {
            @Override
            protected void initComponents() {

            }

            @Override
            public List<? extends ITestComponent> getTestComponents() {
                return Collections.emptyList();
            }
        };
        tTest.init();
        TestReporter.init(tTest);
        op = new AbstractHttpOperation() {

            @Override
            public boolean shouldRunInBackground() {
                return false;
            }

            @Override
            public String getName() {
                return "Test";
            }

            @Override
            protected HttpRequestBuilder getHttpRequestBuilder() {
                return new HttpRequestBuilder()
                        .withBaseUrl("https://reqres.in/api/user/2")
                        .withApiName("getUser")
                        .withMimeType(MimeTypes.APPLICATION_JSON)
                        .withRequestType(HttpMethods.GET);
            }
        };

        failOp = new AbstractHttpOperation() {

            @Override
            public boolean shouldRunInBackground() {
                return false;
            }

            @Override
            public String getName() {
                return "Test";
            }

            @Override
            protected HttpRequestBuilder getHttpRequestBuilder() {
                return new HttpRequestBuilder()
                        .withBaseUrl("https://reqres.in/api/user/2sas")
                        .withApiName("getUser")
                        .withMimeType(MimeTypes.APPLICATION_JSON)
                        .withRequestType(HttpMethods.GET);
            }
        };
    }

    @Test
    public void shouldSucceed() {
        op.execute();
        HttpOpResponse result = op.getResult();
        Assert.assertEquals(result.getExitStatus(), 200);
    }

    @Test
    public void shouldFail() {
        failOp.execute();
        HttpOpResponse result = failOp.getResult();
        Assert.assertNotEquals(result.getExitStatus(), 200);
    }

}