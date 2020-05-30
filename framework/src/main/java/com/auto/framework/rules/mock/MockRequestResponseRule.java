package com.auto.framework.rules.mock;

import com.auto.framework.rules.AbstractMethodRule;
import org.junit.runners.model.FrameworkMethod;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class MockRequestResponseRule extends AbstractMethodRule {
    MockConfig mockConfig = null;

    @Override
    protected void extractTestProperties(FrameworkMethod mTest, Object oTest) {
        MockRequestResponse classAnnotation = oTest.getClass().getAnnotation(MockRequestResponse.class);
        MockRequestResponse methodAnnotation = mTest.getAnnotation(MockRequestResponse.class);
        extractSpecificConfiguration(classAnnotation, methodAnnotation);
    }

    private void extractSpecificConfiguration(MockRequestResponse classAnnotation, MockRequestResponse methodAnnotation) {
        if (null != methodAnnotation) {
            mockConfig = new MockConfig(methodAnnotation.responsePath(), methodAnnotation.url(), methodAnnotation.type(), methodAnnotation.requestPath(), methodAnnotation.withDelay());
        } else if (null != classAnnotation) {
            mockConfig = new MockConfig(classAnnotation.responsePath(), classAnnotation.url(), classAnnotation.type(), classAnnotation.requestPath(), classAnnotation.withDelay());
        }
    }

    public MockConfig getConfig() {
        return mockConfig;
    }
}