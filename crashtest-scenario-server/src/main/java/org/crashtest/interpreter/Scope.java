package org.crashtest.interpreter;

import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Statement;

public interface Scope {
    MethodDef getMethodDef(String methodName);

    void addIdentifier(String name, String value);

    String getIdentifier(String identifierName);

    Scope copy();

    RemoteMethodDef getRemoteMethodDef(String methodName);

    MethodDef addMethodDef(MethodDef def) throws MethodDefinitionException;

    RemoteMethodDef addRemoteMethodDef(RemoteMethodDef def) throws MethodDefinitionException;

    boolean isStatementDefined(Statement statement);
}
