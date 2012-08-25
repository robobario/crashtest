package org.crashtest.request;

import com.google.common.base.Function;

public class ParameterInvocation {
    private String name;

    private String value;

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static final Function<ParameterInvocation,String> GET_NAME = new Function<ParameterInvocation, String>() {
        @Override
        public String apply(ParameterInvocation input) {
            return input.getName();
        }
    };

    public static final Function<ParameterInvocation,String> GET_VALUE = new Function<ParameterInvocation, String>() {
        @Override
        public String apply(ParameterInvocation input) {
            return input.getValue();
        }
    };
}
