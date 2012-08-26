package org.crashtest.service;

import org.crashtest.interpreter.MethodDefinitionException;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.service.model.MethodId;
import org.crashtest.service.model.NoSuchMethodDefinitionException;
import org.crashtest.service.model.RemoteMethodId;

public interface ScopeService {
    MethodId defineMethod(MethodDef def) throws MethodDefinitionException;
    MethodDef getMethodDef(MethodId id) throws NoSuchMethodDefinitionException;

    RemoteMethodId defineRemoteMethod(RemoteMethodDef def) throws MethodDefinitionException;
    RemoteMethodDef getRemoteMethodDef(RemoteMethodId id) throws NoSuchMethodDefinitionException;

    boolean isStatementDefined(Statement statement);

    ScriptExecutor getScriptExecutor();
}
