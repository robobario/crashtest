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

    void addMethodDef(MethodDef def) throws MethodDefinitionException;

    void addRemoteMethodDef(RemoteMethodDef def) throws MethodDefinitionException;

    boolean isStatementDefined(Statement statement);
}
