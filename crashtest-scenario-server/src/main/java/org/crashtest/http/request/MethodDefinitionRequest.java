package org.crashtest.http.request;

import java.util.List;

public class MethodDefinitionRequest {
    private String name;
    private List<ParameterRequest> parameters;
    private List<StatementRequest> statements;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterRequest> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterRequest> parameters) {
        this.parameters = parameters;
    }

    public List<StatementRequest> getStatements() {
        return statements;
    }
}
