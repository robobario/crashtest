package org.crashtest.http.request;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.crashtest.http.request.statements.MethodInvocationRequest;
import org.crashtest.http.request.statements.RemoteInvocationRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value=MethodInvocationRequest.class, name="method invocation"),
        @JsonSubTypes.Type(value=RemoteInvocationRequest.class, name="remote invocation")
})
public interface StatementRequest extends Request{
    public void accept(StatementRequestVisitor visitor);
}
