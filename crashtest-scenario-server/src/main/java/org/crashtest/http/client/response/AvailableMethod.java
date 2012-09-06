package org.crashtest.http.client.response;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.RemoteMethodDef;

import java.util.List;

public class AvailableMethod {
    private List<AvailableParameter> parameters;

    private String name;
    public static final Function<? super AvailableMethod,? extends RemoteMethodDef> TO_REMOTE_METHOD_DEF = new Function<AvailableMethod, RemoteMethodDef>() {
        @Override
        public RemoteMethodDef apply(AvailableMethod input) {
            RemoteMethodDef.Builder remoteDef = RemoteMethodDef.named(input.getName());
            for(AvailableParameter parameter : input.getParameters()){
                remoteDef.withParameterDef(ParameterDef.named(parameter.getName()));
            }
            return remoteDef.build();
        }
    };

    public AvailableMethod() {
    }

    public List<AvailableParameter> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("name",name).add("parameters",parameters).toString();
    }
}
