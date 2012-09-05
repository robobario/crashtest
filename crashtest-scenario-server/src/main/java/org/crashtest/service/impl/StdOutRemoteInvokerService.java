package org.crashtest.service.impl;

import org.crashtest.service.RemoteInvokerService;
import org.crashtest.service.model.RemoteInvocationDescription;

public class StdOutRemoteInvokerService implements RemoteInvokerService{
    @Override
    public void invoke(RemoteInvocationDescription description) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //Do nothing whoa!
        }
        System.out.println(description.prettyPrint());
    }
}
