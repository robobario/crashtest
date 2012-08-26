package org.crashtest.service;

import org.crashtest.interpreter.MethodDefinitionException;
import org.crashtest.interpreter.Scope;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Statement;

public interface ScopeService {
    void addMethodDef(MethodDef def) throws MethodDefinitionException;
    void addRemoteMethodDef(RemoteMethodDef def) throws MethodDefinitionException;
    Scope getGlobalScope();
    boolean isStatementDefined(Statement statement);
}
