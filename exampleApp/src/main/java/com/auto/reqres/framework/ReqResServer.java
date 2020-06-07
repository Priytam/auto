package com.auto.reqres.framework;

import com.auto.framework.AbstractAlwaysRunningTestComponent;
import com.auto.framework.TestComponentData;
import com.auto.framework.operation.OpResult;
import com.auto.framework.reporter.TestReporter;
import com.auto.framework.utils.JsonUtil;
import com.auto.reqres.model.User;
import com.auto.reqres.operation.GetUserOp;
import org.apache.commons.collections.CollectionUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 8:45 am
 * email: mrpjpandey@gmail.com
 */
public class ReqResServer extends AbstractAlwaysRunningTestComponent {

    protected ReqResServer(TestComponentData dData) {
        super(dData);
    }

    @Override
    public void clean(boolean bForce) {
        TestReporter.TRACE("Cleaning component");
    }

    @Override
    public void prepare() {
        TestReporter.TRACE("Preparing component");
    }

    public User getUser(int userId) {
       return getUser(getServer(), getHost(), getPort(), userId);
    }

    public User getUser(String server, String host, int port, int userId) {
        OpResult opResult = performOperation(new GetUserOp(server, host, port, userId));
        if (CollectionUtils.isNotEmpty(opResult.getStdOut())) {
            return JsonUtil.deSerialize(opResult.toStringAsOneLine(), User.class);
        }
        return null;
    }
}
