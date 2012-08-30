package org.crashtest.service.impl;

import org.crashtest.service.RemoteInvokerService;
import org.crashtest.service.model.RemoteInvocationDescription;

public class StdOutRemoteInvokerService implements RemoteInvokerService{
    @Override
    public void invoke(RemoteInvocationDescription description) {
        System.out.println(description.prettyPrint());
    }
}
