package org.crashtest.service.impl;

import com.google.common.collect.ImmutableList;
import org.crashtest.service.ScriptExecutionListener;
import org.crashtest.service.model.ExecutionId;
import org.crashtest.service.model.RemoteInvocationDescription;

import java.util.List;

public class ScriptExecutionRecorder implements ScriptExecutionListener {
    private ExecutionId id;

    private List<RemoteInvocationDescription> successfulInvocations = ImmutableList.of();

    private List<String> errors = ImmutableList.of();

    private boolean isComplete = false;

    private ScriptExecutionRecorder(ExecutionId id){
       this.id = id;
    }

    @Override
    public void onRemoteMethodInvocationSuccess(RemoteInvocationDescription success) {
        successfulInvocations = ImmutableList.<RemoteInvocationDescription>builder().addAll(successfulInvocations).add(success).build();
    }

    @Override
    public void onRemoteMethodInvocationError(String error) {
        errors = ImmutableList.<String>builder().addAll(errors).add(error).build();
    }

    @Override
    public void onCompletion() {
        isComplete = true;
    }

    public static ScriptExecutionRecorder forId(ExecutionId id){
        return new ScriptExecutionRecorder(id);
    }

    public ExecutionId getId() {
        return id;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
