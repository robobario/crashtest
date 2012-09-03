package org.crashtest.interpreter;

import org.crashtest.interpreter.impl.SimpleScope;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.junit.Test;

public class SimpleScopeTest {

    @Test
    public void testDefineMethodWithRemoteStatement() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
        build.addMethodDef(MethodDef.named("method").withStatement(RemoteInvocation.named("remote").build()).build());
    }

    @Test
    public void testDefineMethodWithMethodStatement() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addMethodDef(MethodDef.named("method").build());
        build.addMethodDef(MethodDef.named("method2").withStatement(MethodInvocation.named("method").build()).build());
    }

    @Test
    public void testDefineMethodWithMultipleStatements() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addMethodDef(MethodDef.named("method").build());
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
        build.addMethodDef(MethodDef.named("method2").withStatement(RemoteInvocation.named("remote").build()).withStatement(MethodInvocation.named("method").build()).build());
    }

    @Test(expected = MethodDefinitionException.class)
    public void testDefineMethodTooManyParameters_Remote() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
        build.addMethodDef(MethodDef.named("method").withStatement(RemoteInvocation.named("remote").withParameterExpression(Literal.of("hi")).build()).build());
    }

    @Test(expected = MethodDefinitionException.class)
    public void testDefineMethodNotEnoughParameters_Remote() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").withParameterDef(ParameterDef.named("foo")).build());
        build.addMethodDef(MethodDef.named("method").withStatement(RemoteInvocation.named("remote").build()).build());
    }

    @Test(expected = MethodDefinitionException.class)
    public void testDefineMethodTooManyParameters_Method() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addMethodDef(MethodDef.named("method").build());
        build.addMethodDef(MethodDef.named("method2").withStatement(MethodInvocation.named("method").withParameterExpression(Literal.of("hi")).build()).build());
    }

    @Test(expected = MethodDefinitionException.class)
    public void testDefineMethodNotEnoughParameters_Method() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addMethodDef(MethodDef.named("method").withParameter(ParameterDef.named("foo")).build());
        build.addMethodDef(MethodDef.named("method2").withStatement(MethodInvocation.named("method").build()).build());
    }

    @Test(expected = MethodDefinitionException.class)
    public void testDefineMethodThatCallsNonExistantRemoteMethod() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
        build.addMethodDef(MethodDef.named("method").withStatement(RemoteInvocation.named("badongo").build()).build());
    }

    @Test(expected = MethodDefinitionException.class)
    public void testDefineMethodThatCallsNonExistantMethod() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
        build.addMethodDef(MethodDef.named("method").withStatement(MethodInvocation.named("badongo").build()).build());
    }


    @Test(expected = MethodDefinitionException.class)
    public void testDefineMethodWithExistingName() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
        build.addMethodDef(MethodDef.named("method").withStatement(RemoteInvocation.named("remote").build()).build());
        build.addMethodDef(MethodDef.named("method").withStatement(RemoteInvocation.named("remote").build()).build());
    }

    @Test(expected = MethodDefinitionException.class)
    public void testDefineRemoteMethodWithExistingName() throws MethodDefinitionException {
        SimpleScope build = SimpleScope.builder().build();
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
        build.addRemoteMethodDef(RemoteMethodDef.named("remote").build());
    }
}
