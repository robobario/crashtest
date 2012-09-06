package org.crashtest.http.server.request.statements;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.crashtest.http.server.request.ParameterExpressionRequest;
import org.crashtest.http.server.request.Requests;
import org.crashtest.http.server.request.StatementRequest;
import org.crashtest.http.server.request.StatementRequestVisitor;

import java.util.List;

public class MethodInvocationRequest implements StatementRequest {
    private List<ParameterExpressionRequest> parameterExpressions = ImmutableList.of();
    private String name;

    @Override
    public void accept(StatementRequestVisitor visitor) {
        visitor.visit(this);
    }

    public List<ParameterExpressionRequest> getParameterExpressions() {
        return parameterExpressions;
    }

    public void setParameterExpressions(List<ParameterExpressionRequest> parameterExpressions) {
        this.parameterExpressions = parameterExpressions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(name) && Iterables.all(parameterExpressions, Requests.IS_VALID);
    }
}
