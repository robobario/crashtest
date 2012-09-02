package org.crashtest.service.impl;

import com.google.common.collect.ImmutableMap;
import org.crashtest.service.model.ExecutionId;

import java.util.Map;

public class ScriptExecutionRecorderService {
    private static final ScriptExecutionRecorderService service = new ScriptExecutionRecorderService();

    private Map<ExecutionId, ScriptExecutionRecorder> executions = ImmutableMap.of();

    private ScriptExecutionRecorderService(){}

    public synchronized ScriptExecutionRecorder getNewRecorder() {
        ExecutionId nextId = ExecutionId.of(executions.size() + 1);
        ScriptExecutionRecorder scriptExecutionRecorder = ScriptExecutionRecorder.forId(nextId);
        executions = ImmutableMap.<ExecutionId,ScriptExecutionRecorder>builder().putAll(executions).put(nextId,scriptExecutionRecorder).build();
        return scriptExecutionRecorder;
    }

    public static ScriptExecutionRecorderService instance(){
        return service;
    }
}


