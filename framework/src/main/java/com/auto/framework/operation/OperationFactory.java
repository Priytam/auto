package com.auto.framework.operation;

import com.google.common.collect.Lists;

import java.util.List;
/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class OperationFactory {

    public static final String OK = "Ok";

    public static Operation consumableOp(Caller caller, String opName) {
        return new Operation() {
            long  startTime;
            @Override
            public void execute() {
                startTime = System.currentTimeMillis();
                caller.call();
            }

            @Override
            public OpRequest getRequest() {
                return caller::toString;
            }

            @Override
            public OpResult getResult() {
                return new OpResult() {
                    @Override
                    public int getExitStatus() {
                        return 0;
                    }

                    @Override
                    public List<String> getStdOut() {
                        return null;
                    }

                    @Override
                    public List<String> getStdErr() {
                        return Lists.newArrayList(OK);
                    }

                    @Override
                    public long getExecutionTime() {
                        return  System.currentTimeMillis() - startTime;
                    }

                    @Override
                    public String toStringAsOneLine() {
                        return OK;
                    }
                };
            }

            @Override
            public boolean shouldRunInBackground() {
                return false;
            }

            @Override
            public String getName() {
                return opName;
            }
        };
    }
}
