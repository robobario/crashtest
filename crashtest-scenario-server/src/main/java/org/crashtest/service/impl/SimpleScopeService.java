package org.crashtest.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.crashtest.interpreter.MethodDefinitionException;
import org.crashtest.interpreter.Scope;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.SimpleScope;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.service.ScopeService;
import org.crashtest.service.model.MethodId;
import org.crashtest.service.model.NoSuchMethodDefinitionException;
import org.crashtest.service.model.RemoteMethodId;

import java.util.List;
import java.util.Map;

public class SimpleScopeService implements ScopeService{
    private Scope scope;
    private Map<MethodId,MethodDef> methodIdToMethodDef = ImmutableMap.of();
    private Map<RemoteMethodId,RemoteMethodDef> remoteMethodIdToMethodDef = ImmutableMap.of();
    private static final SimpleScopeService instance = new SimpleScopeService(SimpleScope.builder().build());

    public SimpleScopeService(Scope scope) {
        this.scope = scope;
    }

    @Override
    public synchronized MethodId defineMethod(MethodDef def) throws MethodDefinitionException {
        MethodDef newMethodDef = scope.addMethodDef(def);
        MethodId nextId = MethodId.of(methodIdToMethodDef.size() + 1);
        methodIdToMethodDef = ImmutableMap.<MethodId,MethodDef>builder().putAll(methodIdToMethodDef).put(nextId, newMethodDef).build();
        return nextId;
    }

    @Override
    public MethodDef getMethodDef(MethodId id) throws NoSuchMethodDefinitionException {
        if(methodIdToMethodDef.containsKey(id)){
            return methodIdToMethodDef.get(id);
        }else{
            throw new NoSuchMethodDefinitionException("no method with id " + id + " is defined");
        }
    }

    @Override
    public List<MethodId> getAllMethodIds() {
        return ImmutableList.copyOf(methodIdToMethodDef.keySet());
    }


    @Override
    public synchronized RemoteMethodId defineRemoteMethod(RemoteMethodDef def) throws MethodDefinitionException {
        RemoteMethodDef newMethodDef = scope.addRemoteMethodDef(def);
        RemoteMethodId nextId = RemoteMethodId.of(remoteMethodIdToMethodDef.size() + 1);
        remoteMethodIdToMethodDef = ImmutableMap.<RemoteMethodId,RemoteMethodDef>builder().putAll(remoteMethodIdToMethodDef).put(nextId, newMethodDef).build();
        return nextId;
    }

    @Override
    public RemoteMethodDef getRemoteMethodDef(RemoteMethodId id) throws NoSuchMethodDefinitionException {
        if(remoteMethodIdToMethodDef.containsKey(id)){
            return remoteMethodIdToMethodDef.get(id);
        }else{
            throw new NoSuchMethodDefinitionException("no remote method with id " + id + " is defined");
        }
    }

    @Override
    public List<RemoteMethodId> getAllRemoteMethodIds() {
        return ImmutableList.copyOf(remoteMethodIdToMethodDef.keySet());
    }


    @Override
    public boolean isStatementDefined(Statement statement) {
        return scope.isStatementDefined(statement);
    }

    @Override
    public ScriptExecutor getScriptExecutor() {
        return new ScriptExecutor(new StdOutRemoteInvokerService(),scope.copy());
    }

    public static ScopeService instance() {
        return instance;
    }
}
