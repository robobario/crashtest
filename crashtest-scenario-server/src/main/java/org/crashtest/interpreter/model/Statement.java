package org.crashtest.interpreter.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value=MethodInvocation.class, name="method invocation"),
        @JsonSubTypes.Type(value=RemoteInvocation.class, name="remote invocation")
})
public interface Statement {
    public void accept(StatementVisitor visitor);

    List<Expression> getParameterExpressions();
}
