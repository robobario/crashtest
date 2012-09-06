package org.crashtest.service.impl;

import com.google.common.collect.ImmutableList;
import org.crashtest.service.RemoteInvokerService;
import org.crashtest.service.model.RemoteInvocationDescription;

import java.util.List;

public class StdOutRemoteInvokerService implements RemoteInvokerService{
    @Override
    public List<ErrorMessage> invoke(RemoteInvocationDescription description) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //Do nothing whoa!
        }
        System.out.println(description.prettyPrint());
        return ImmutableList.of();
    }
}
