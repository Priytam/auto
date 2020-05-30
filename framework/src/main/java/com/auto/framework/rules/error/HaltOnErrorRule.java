package com.auto.framework.rules.error;

import com.auto.framework.rules.AbstractMethodRule;
import org.junit.runners.model.FrameworkMethod;

import java.util.Objects;

/*
@author : 'Vipul Popli'
*/
public class HaltOnErrorRule extends AbstractMethodRule {
    private boolean haltOnError = false;

    @Override
    protected void extractTestProperties(FrameworkMethod mTest, Object oTest) {
        HaltOnError classAnnotation = oTest.getClass().getAnnotation(HaltOnError.class);
        HaltOnError methodAnnotation = mTest.getAnnotation(HaltOnError.class);
        if(Objects.nonNull(classAnnotation) || Objects.nonNull(methodAnnotation)){
            this.haltOnError = true;
        }
    }

    public boolean isHaltOnError() {
        return haltOnError;
    }
}
