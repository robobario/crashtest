package org.crashtest.service;

import org.crashtest.service.model.RemoteInvocationDescription;

public interface ScriptExecutionListener {
    void onRemoteMethodInvocationSuccess(RemoteInvocationDescription build);
    void onRemoteMethodInvocationError(String error);
}
