package com.auto.framework.rules.logging;

import com.auto.framework.rules.AbstractMethodRule;
import org.junit.runners.model.FrameworkMethod;

import java.util.Objects;

/*
@author : 'Vipul Popli'
*/
public class KeepLogRule extends AbstractMethodRule {
    private boolean keepLog = true;

    @Override
    protected void extractTestProperties(FrameworkMethod mTest, Object oTest) {
        KeepLog classAnnotation = oTest.getClass().getAnnotation(KeepLog.class);
        KeepLog methodAnnotation = mTest.getAnnotation(KeepLog.class);
        if(Objects.nonNull(methodAnnotation)){
            this.keepLog = methodAnnotation.keepLog();
        }else if(Objects.nonNull(classAnnotation)){
            this.keepLog = classAnnotation.keepLog();
        }
    }

    public boolean keepLog() {
        return keepLog;
    }
}
