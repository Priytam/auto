package com.auto.framework.runner.report;

import com.auto.framework.runner.data.ExecutionResult;

import java.util.function.Consumer;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public interface TestJobReporter extends Consumer<ExecutionResult> {
}
