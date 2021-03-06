package org.crashtest.http.server.request;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.crashtest.http.server.request.expressions.IdentifierRequest;
import org.crashtest.http.server.request.expressions.LiteralRequest;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value=IdentifierRequest.class, name="identifier"),
        @JsonSubTypes.Type(value=LiteralRequest.class, name="literal")
})
public interface ParameterExpressionRequest extends Request {
    public void accept(ExpressionRequestVisitor visitor);
}
