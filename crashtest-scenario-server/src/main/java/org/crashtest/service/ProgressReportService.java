package org.crashtest.service;

import org.crashtest.service.model.ExecutionId;

public interface ProgressReportService {
    ExecutionId addNewProgressReportProvider(ProgressReportProvider provider);
    ProgressReportProvider getProviderFor(ExecutionId id);
}
