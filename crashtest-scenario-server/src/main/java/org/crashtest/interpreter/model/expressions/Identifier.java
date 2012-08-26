package org.crashtest.interpreter.model.expressions;

import com.google.common.base.Objects;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.ExpressionVisitor;

public class Identifier implements Expression {
    public String getIdentifierName() {
        return identifierName;
    }

    String identifierName;

    private Identifier(String identifierName) {
        this.identifierName = identifierName;
    }

    public static Identifier named(String name){
        return new Identifier(name);
    }

    @Override
    public String toString(){
        return Objects.toStringHelper(this).add("identifierName",identifierName).toString();
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }
}
