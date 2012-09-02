package org.crashtest.service.impl;

import org.crashtest.service.model.ExecutionId;

public class ScriptExecutionRecorderService {
    public ScriptExecutionRecorder getNewRecorder() {
        return ScriptExecutionRecorder.forId(ExecutionId.of(1l));
    }
}


