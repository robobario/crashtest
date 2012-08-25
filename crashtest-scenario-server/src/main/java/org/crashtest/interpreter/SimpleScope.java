package org.crashtest.interpreter;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.crashtest.model.MethodDef;
import org.crashtest.model.RemoteMethodDef;

public class SimpleScope implements Scope {
    private ImmutableMap<String, String> identifiers = ImmutableMap.of();
    private ImmutableMap<String, MethodDef> methods = ImmutableMap.of();
    private ImmutableMap<String, RemoteMethodDef> remoteMethods = ImmutableMap.of();

    private SimpleScope(ImmutableMap<String, String> identifiers) {
        this.identifiers = identifiers;
    }

    private SimpleScope(ImmutableMap<String, String> identifiers, ImmutableMap<String, MethodDef> methods, ImmutableMap<String, RemoteMethodDef> remoteMethods) {
        this.identifiers = identifiers;
        this.methods = methods;
        this.remoteMethods = remoteMethods;
    }

    @Override
    public MethodDef getMethodDef(String methodNAme) {
        return methods.get(methodNAme);
    }

    @Override
    public void addIdentifier(String name, String value) {
        identifiers = ImmutableMap.<String, String>builder().putAll(identifiers).put(name, value).build();
    }

    @Override
    public String getIdentifier(String identifierName) {
        return identifiers.get(identifierName);
    }

    @Override
    public Scope copy() {
        return new SimpleScope(ImmutableMap.copyOf(identifiers),ImmutableMap.copyOf(methods),ImmutableMap.copyOf(remoteMethods));
    }

    @Override
    public RemoteMethodDef getRemoteMethodDef(String methodName) {
        return remoteMethods.get(methodName);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        ImmutableMap.Builder<String, String> identifierBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, MethodDef> methodDefBuilder = ImmutableMap.builder();
        ImmutableMap.Builder<String, RemoteMethodDef> remoteMethodDefBuilder = ImmutableMap.builder();

        private Builder() {

        }

        public Builder withMethod(MethodDef def) {
            methodDefBuilder.put(def.getName(), def);
            return this;
        }

        public Builder withMethods(Iterable<MethodDef> def) {
            methodDefBuilder.putAll(Maps.uniqueIndex(def, new Function<MethodDef, String>() {
                @Override
                public String apply(MethodDef input) {
                    return input.getName();
                }
            }));
            return this;
        }

        public Builder withRemoteMethod(RemoteMethodDef def) {
            remoteMethodDefBuilder.put(def.getName(), def);
            return this;
        }

        public Builder withRemoteMethods(Iterable<RemoteMethodDef> defs) {
            remoteMethodDefBuilder.putAll(Maps.uniqueIndex(defs, new Function<RemoteMethodDef, String>() {
                @Override
                public String apply(RemoteMethodDef input) {
                    return input.getName();
                }
            }));
            return this;
        }

        public SimpleScope build() {
            return new SimpleScope(identifierBuilder.build(), methodDefBuilder.build(), remoteMethodDefBuilder.build());
        }

    }
}
