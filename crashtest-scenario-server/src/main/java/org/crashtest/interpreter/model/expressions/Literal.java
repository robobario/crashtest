package org.crashtest.interpreter.model.expressions;

import com.google.common.base.Objects;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.ExpressionVisitor;

public class Literal implements Expression {
    public String getValue() {
        return value;
    }

    String value;

    private Literal(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return Objects.toStringHelper(this).add("value",value).toString();
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Literal){
            final Literal other = (Literal) obj;
            return Objects.equal(value, other.value);
        } else{
            return false;
        }
    }

    public static Literal of(String value){
        return new Literal(value);
    }
}
