package com.auto.framework.rules.tags;

import com.auto.framework.rules.AbstractMethodRule;
import joptsimple.internal.Strings;
import org.junit.runners.model.FrameworkMethod;

import java.util.Objects;

public class TagsRule extends AbstractMethodRule {
    private String[] tags;

    @Override
    protected void extractTestProperties(FrameworkMethod mTest, Object oTest) {
        Tags classAnnotation = oTest.getClass().getAnnotation(Tags.class);
        Tags methodAnnotation = mTest.getAnnotation(Tags.class);
        if (methodAnnotation != null) {
            tags = methodAnnotation.value();
        } else if (classAnnotation != null) {
            tags = classAnnotation.value();
        }
    }

    public String getTags() {
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(tags)) {
            for (String str : tags) {
                sb.append(str).append(",");
            }
            return sb.substring(0, sb.length() - 1);
        }
        return Strings.EMPTY;
    }
}
