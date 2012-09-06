package org.crashtest.http.server.request;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

public class MethodDefinitionRequest implements Request {
    private String name;
    private List<ParameterRequest> parameters = ImmutableList.of();
    private List<StatementRequest> statements = ImmutableList.of();

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

    public void setStatements(List<StatementRequest> statements) {
        this.statements = statements;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(name) && Iterables.all(parameters,Requests.IS_VALID) && Iterables.all(statements,Requests.IS_VALID);
    }
}
