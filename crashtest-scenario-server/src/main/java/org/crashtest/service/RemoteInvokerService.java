package org.crashtest.service;

import org.crashtest.service.model.RemoteInvocationDescription;

public interface RemoteInvokerService {
    public void invoke(RemoteInvocationDescription description);
}
