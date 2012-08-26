package org.crashtest.interpreter;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.interpreter.model.StatementVisitor;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;

import java.util.List;

public class SimpleScope implements Scope {
    private ImmutableMap<String, String> identifiers = ImmutableMap.of();
    private ImmutableMap<String, MethodDef> methods = ImmutableMap.of();
    private ImmutableMap<String, RemoteMethodDef> remoteMethods = ImmutableMap.of();

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
    public void addMethodDef(MethodDef def) throws MethodDefinitionException {
        if(methodCanBeAdded(def)){
            methods = ImmutableMap.<String, MethodDef>builder().putAll(methods).put(def.getName(), def).build();
        }
    }

    @Override
    public void addRemoteMethodDef(RemoteMethodDef def) throws MethodDefinitionException {
        if(remoteMethodCanBeAdded(def)){
            remoteMethods = ImmutableMap.<String, RemoteMethodDef>builder().putAll(remoteMethods).put(def.getName(), def).build();
        }
    }

    @Override
    public boolean isStatementDefined(Statement statement) {
        final List<Boolean> isDefined = Lists.newArrayList();
        statement.accept(new StatementVisitor() {
            @Override
            public void visit(MethodInvocation invocation) {
                MethodDef methodDef = getMethodDef(invocation.getMethodName());
                isDefined.add(null != methodDef && methodDef.getParameters().size() != invocation.getParameterExpressions().size());
            }

            @Override
            public void visit(RemoteInvocation invocation) {
                RemoteMethodDef def = getRemoteMethodDef(invocation.getMethodName());
                isDefined.add(null != def && def.getParameters().size() != invocation.getParameterExpressions().size());
            }
        });
        return Iterables.getOnlyElement(isDefined);
    }

    private boolean remoteMethodCanBeAdded(RemoteMethodDef def) throws MethodDefinitionException {
        if(remoteMethods.containsKey(def.getName())){
            throw new MethodDefinitionException("remote method already defined : "+def.getName());
        }
        return true;
    }


    private boolean methodCanBeAdded(MethodDef def) throws MethodDefinitionException {
        if(methods.containsKey(def.getName())){
            throw new MethodDefinitionException("method already defined : "+def.getName());
        }
        for(Statement statement: def.getStatements()){
            final List<MethodDefinitionException> exceptions = Lists.newArrayList();
            statement.accept(new StatementVisitor() {
                @Override
                public void visit(MethodInvocation invocation) {
                    MethodDef methodDef = getMethodDef(invocation.getMethodName());
                    if(null == methodDef) exceptions.add(new MethodDefinitionException("a statement in the method you are defining does not correspond to a defined rule : " + invocation.getMethodName()));
                    else if(methodDef.getParameters().size() != invocation.getParameterExpressions().size()){
                        exceptions.add(new MethodDefinitionException("a statement in the method you are defining has the wrong number of arguments, invocation : " + invocation.toString() + ", def : " + methodDef.toString()));
                    }
                }

                @Override
                public void visit(RemoteInvocation invocation) {
                    RemoteMethodDef def = getRemoteMethodDef(invocation.getMethodName());
                    if(null == def) exceptions.add(new MethodDefinitionException("a statement in the method you are defining does not correspond to a defined remote rule : " + invocation.getMethodName()));
                    else if(def.getParameters().size() != invocation.getParameterExpressions().size()){
                        exceptions.add(new MethodDefinitionException("a statement in the method you are defining has the wrong number of arguments, invocation : " + invocation.toString() + ", def : " + def.toString()));
                    }
                }
            });
            if(!exceptions.isEmpty()){
                throw exceptions.iterator().next();
            }
        }
        return true;
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
