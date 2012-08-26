package org.crashtest.service.impl;

import org.crashtest.interpreter.MethodDefinitionException;
import org.crashtest.interpreter.Scope;
import org.crashtest.interpreter.SimpleScope;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.service.ScopeService;

public class SimpleScopeService implements ScopeService{
    Scope scope = SimpleScope.builder().build();

    private static final SimpleScopeService instance = new SimpleScopeService();

    @Override
    public void addMethodDef(MethodDef def) throws MethodDefinitionException {
         scope.addMethodDef(def);
    }

    @Override
    public void addRemoteMethodDef(RemoteMethodDef def) throws MethodDefinitionException {
         scope.addRemoteMethodDef(def);
    }

    @Override
    public Scope getGlobalScope() {
        return scope.copy();
    }

    @Override
    public boolean isStatementDefined(Statement statement) {
        return scope.isStatementDefined(statement);
    }

    public static ScopeService instance() {
        return instance;
    }
}
