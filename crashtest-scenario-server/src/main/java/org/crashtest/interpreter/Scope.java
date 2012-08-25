package org.crashtest.interpreter;

import org.crashtest.model.MethodDef;
import org.crashtest.model.RemoteMethodDef;

public interface Scope {
    MethodDef getMethodDef(String methodName);

    void addIdentifier(String name, String value);

    String getIdentifier(String identifierName);

    Scope copy();

    RemoteMethodDef getRemoteMethodDef(String methodName);
}
