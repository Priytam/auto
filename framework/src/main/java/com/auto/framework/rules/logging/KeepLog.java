package com.auto.framework.rules.logging;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Inherited
public @interface KeepLog {
    boolean keepLog() default true;
}
