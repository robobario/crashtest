package org.crashtest.interpreter.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value=Identifier.class, name="identifier"),
        @JsonSubTypes.Type(value=Literal.class, name="literal")
})
public interface Expression {
    void accept(ExpressionVisitor visitor);
}
