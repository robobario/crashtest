package org.crashtest.service.impl;

import org.crashtest.service.model.ExecutionId;

public class ScriptExecutionRecorderService {
    private static final ScriptExecutionRecorderService service = new ScriptExecutionRecorderService();

    private ScriptExecutionRecorderService(){}

    public ScriptExecutionRecorder getNewRecorder() {
        return ScriptExecutionRecorder.forId(ExecutionId.of(1l));
    }

    public static ScriptExecutionRecorderService instance(){
        return service;
    }
}


