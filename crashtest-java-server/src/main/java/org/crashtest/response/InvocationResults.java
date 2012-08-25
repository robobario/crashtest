package org.crashtest.response;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

public class InvocationResults {
    private InvocationResults(Collection<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    private Collection<String> errorMessages;

    public String toString(){
        return Objects.toStringHelper(this).add("errorMessages",errorMessages).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(errorMessages);
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof InvocationResults){
            final InvocationResults other = (InvocationResults) obj;
            return Objects.equal(errorMessages, other.errorMessages);
        } else{
            return false;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private ImmutableList.Builder<String> errors;

        private Builder(){
            errors = ImmutableList.builder();
        }

        public Builder withErrorMessage(String message){
            errors.add(message);
            return this;
        }

        public Builder withErrorMessages(Iterable<String> errorMessages){
            errors.addAll(errorMessages);
            return this;
        }

        public InvocationResults build(){
            return new InvocationResults(errors.build());
        }
    }

}
