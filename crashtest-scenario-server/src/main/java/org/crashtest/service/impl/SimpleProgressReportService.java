package org.crashtest.service.impl;

import com.google.common.collect.ImmutableMap;
import org.crashtest.service.ProgressReportProvider;
import org.crashtest.service.ProgressReportService;
import org.crashtest.service.model.ExecutionId;

import java.util.Map;

public class SimpleProgressReportService implements ProgressReportService {
    private static final ProgressReportService SERVICE = new SimpleProgressReportService();

    private Map<ExecutionId, ProgressReportProvider> executions = ImmutableMap.of();

    private SimpleProgressReportService(){}

    @Override
    public synchronized ExecutionId addNewProgressReportProvider(ProgressReportProvider provider) {
        ExecutionId nextId = ExecutionId.of(executions.size() + 1);
        executions = ImmutableMap.<ExecutionId,ProgressReportProvider>builder().putAll(executions).put(nextId,provider).build();
        return nextId;
    }

    @Override
    public ProgressReportProvider getProviderFor(ExecutionId id) {
        return executions.get(id);
    }

    public static ProgressReportService instance(){
        return SERVICE;
    }
}


