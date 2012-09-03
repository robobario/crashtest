package org.crashtest.service.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.crashtest.common.model.ParameterDescription;

import java.util.List;

public interface ProgressTreeNode {
    @JsonProperty
    public List<ProgressTreeNode> getChildren();
    @JsonProperty
    public boolean isCompleted();
    @JsonProperty
    public String getName();
    @JsonProperty
    public List<ParameterDescription> getParameters();
    @JsonProperty
    public List<Exception> getExceptions();
}
