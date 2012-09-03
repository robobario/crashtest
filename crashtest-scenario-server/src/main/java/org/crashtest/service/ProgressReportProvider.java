package org.crashtest.service;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.crashtest.service.model.ProgressTreeNode;

@JsonAutoDetect
public interface ProgressReportProvider {
    @JsonProperty
    public ProgressTreeNode getProgressTree();
}
