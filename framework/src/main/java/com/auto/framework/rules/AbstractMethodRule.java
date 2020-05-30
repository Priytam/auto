package com.auto.framework.rules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractMethodRule implements MethodRule {

	@Override
	public final Statement apply(final Statement arg0, FrameworkMethod arg1, Object arg2)
	{
		extractTestProperties(arg1, arg2);
		return arg0;
	}

	protected abstract void extractTestProperties(FrameworkMethod mTest, Object oTest);
}
