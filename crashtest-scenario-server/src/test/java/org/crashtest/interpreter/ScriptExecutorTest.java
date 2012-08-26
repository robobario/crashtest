package org.crashtest.interpreter;

import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.crashtest.service.model.ParameterDescription;
import org.crashtest.service.model.RemoteInvocationDescription;
import org.crashtest.service.RemoteInvokerService;
import org.junit.Test;
import org.mockito.Mockito;

public class ScriptExecutorTest {

    private static final String SCRIPT_NAME = "funk-script";
    private static final String REMOTE_METHOD_NAME = "remote";
    private static final String METHOD_NAME = "method";

    @Test
    public void testRemoteInvocationScript() throws ScriptExecutionException {
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new ScriptExecutor(service);
        Script script = Script.named(SCRIPT_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).build()).build();
        scriptExecutor.executeScript(script, scope);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).build());
    }

    @Test
    public void testRemoteInvocationScriptWithALiteral() throws ScriptExecutionException {
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).withParameterDef(ParameterDef.named("x")).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new ScriptExecutor(service);
        Script script = Script.named(SCRIPT_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).withParameterExpression(Literal.of("foo")).build()).build();
        scriptExecutor.executeScript(script, scope);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).withParameter(ParameterDescription.create("x", "foo")).build());
    }

    @Test
    public void testMethodInvocationScript() throws ScriptExecutionException {
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).build()).withMethod(MethodDef.named(METHOD_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).build()).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new ScriptExecutor(service);
        Script script = Script.named(SCRIPT_NAME).withStatement(MethodInvocation.named(METHOD_NAME).build()).build();
        scriptExecutor.executeScript(script, scope);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).build());
    }

    @Test
    public void testMethodWithVariableInvocationScript() throws ScriptExecutionException {
        SimpleScope.Builder builder = SimpleScope.builder();
        builder.withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).withParameterDef(ParameterDef.named("bar")).build());
        builder.withMethod(MethodDef.named(METHOD_NAME).withParameter(ParameterDef.named("foo")).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).withParameterExpression(Identifier.named("foo")).build()).build());
        SimpleScope scope = builder.build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new ScriptExecutor(service);
        Script script = Script.named(SCRIPT_NAME).withStatement(MethodInvocation.named(METHOD_NAME).withParameterExpression(Literal.of("howdy")).build()).build();
        scriptExecutor.executeScript(script, scope);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).withParameter(ParameterDescription.create("bar", "howdy")).build());
    }

}
