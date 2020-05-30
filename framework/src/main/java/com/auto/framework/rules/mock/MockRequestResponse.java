package com.auto.framework.rules.mock;

import java.lang.annotation.*;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Inherited
public @interface MockRequestResponse {
    RequestType type() default RequestType.GET;
    String url();
    String responsePath();
    String requestPath();
    int withDelay() default 0;

}
