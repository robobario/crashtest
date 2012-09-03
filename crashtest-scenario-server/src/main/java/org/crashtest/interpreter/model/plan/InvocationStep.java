package org.crashtest.interpreter.model.plan;

import org.crashtest.common.model.ParameterDescription;

import java.util.List;

public interface InvocationStep {
    void accept(InvocationStepVisitor visitor);

    String getName();

    List<ParameterDescription> getParameters();
}
