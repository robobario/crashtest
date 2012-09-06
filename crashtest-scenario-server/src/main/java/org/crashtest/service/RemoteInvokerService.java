package org.crashtest.service;

import org.crashtest.service.impl.ErrorMessage;
import org.crashtest.service.model.RemoteInvocationDescription;

import java.util.List;

public interface RemoteInvokerService {
    public List<ErrorMessage> invoke(RemoteInvocationDescription description);
}
