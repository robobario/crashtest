package org.crashtest.interpreter;

import org.crashtest.model.MethodDef;
import org.crashtest.model.RemoteMethodDef;
import org.crashtest.model.Script;
import org.crashtest.model.statements.MethodInvocation;
import org.crashtest.model.statements.RemoteInvocation;
import org.crashtest.service.RemoteInvocationDescription;
import org.crashtest.service.RemoteInvokerService;
import org.junit.Test;
import org.mockito.Mockito;

public class ScriptExecutorTest {

    private static final String SCRIPT_NAME = "funk-script";
    private static final String REMOTE_METHOD_NAME = "remote";
    private static final String METHOD_NAME = "method";

    @Test
    public void testRemoteInvocationScript(){
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new ScriptExecutor(service);
        Script script = Script.named(SCRIPT_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).build()).build();
        scriptExecutor.executeScript(script,scope);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).build());
    }

    @Test
    public void testMethodInvocationScript(){
        SimpleScope scope = SimpleScope.builder().withRemoteMethod(RemoteMethodDef.named(REMOTE_METHOD_NAME).build()).withMethod(MethodDef.named(METHOD_NAME).withStatement(RemoteInvocation.named(REMOTE_METHOD_NAME).build()).build()).build();
        RemoteInvokerService service = Mockito.mock(RemoteInvokerService.class);
        ScriptExecutor scriptExecutor = new ScriptExecutor(service);
        Script script = Script.named(SCRIPT_NAME).withStatement(MethodInvocation.named(METHOD_NAME).build()).build();
        scriptExecutor.executeScript(script,scope);
        Mockito.verify(service).invoke(RemoteInvocationDescription.named(REMOTE_METHOD_NAME).build());
    }
}
