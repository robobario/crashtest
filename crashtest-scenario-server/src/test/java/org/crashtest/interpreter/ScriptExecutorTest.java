package org.crashtest.interpreter;

import org.crashtest.interpreter.impl.SimpleScope;
import org.crashtest.interpreter.impl.SimpleScriptExecutor;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.crashtest.common.model.ParameterDescription;
import org.crashtest.service.model.RemoteInvocationDescription;
import org.crashtest.service.RemoteInvokerService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScriptExecutorTest {

    private static final String SCRIPT_NAME = "funk-script";
    private static final String REMOTE_METHOD_NAME = "remote";
    private static final String METHOD_NAME = "method";

    @Test
    public void testRemoteInvocationScript() throws ScriptExecutionException, ExecutionException, TimeoutException, InterruptedException {
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new SimpleScriptExecutor(service,scope);
        Script script = Script.named(SCRIPT_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).build()).build();
        Future<?> future = scriptExecutor.executeScript(script).getExecutionFuture();
        future.get(10, TimeUnit.SECONDS);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).build());
    }

    @Test
    public void testRemoteInvocationScriptWithALiteral() throws ScriptExecutionException, ExecutionException, TimeoutException, InterruptedException {
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).withParameterDef(ParameterDef.named("x")).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new SimpleScriptExecutor(service,scope);
        Script script = Script.named(SCRIPT_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).withParameterExpression(Literal.of("foo")).build()).build();
        Future<?> future = scriptExecutor.executeScript(script).getExecutionFuture();
        future.get(10, TimeUnit.SECONDS);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).withParameter(ParameterDescription.create("x", "foo")).build());
    }

    @Test
    public void testMethodInvocationScript() throws ScriptExecutionException, ExecutionException, TimeoutException, InterruptedException {
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).build()).withMethod(MethodDef.named(METHOD_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).build()).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new SimpleScriptExecutor(service,scope);
        Script script = Script.named(SCRIPT_NAME).withStatement(MethodInvocation.named(METHOD_NAME).build()).build();
        Future<?> future = scriptExecutor.executeScript(script).getExecutionFuture();
        future.get(10, TimeUnit.SECONDS);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).build());
    }

    @Test
    public void testMethodWithVariableInvocationScript() throws ScriptExecutionException, ExecutionException, TimeoutException, InterruptedException {
        SimpleScope.Builder builder = SimpleScope.builder();
        builder.withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).withParameterDef(ParameterDef.named("bar")).build());
        builder.withMethod(MethodDef.named(METHOD_NAME).withParameter(ParameterDef.named("foo")).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).withParameterExpression(Identifier.named("foo")).build()).build());
        SimpleScope scope = builder.build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new SimpleScriptExecutor(service,scope);
        Script script = Script.named(SCRIPT_NAME).withStatement(MethodInvocation.named(METHOD_NAME).withParameterExpression(Literal.of("howdy")).build()).build();
        Future<?> future = scriptExecutor.executeScript(script).getExecutionFuture();
        future.get(10, TimeUnit.SECONDS);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).withParameter(ParameterDescription.create("bar", "howdy")).build());
    }

}
