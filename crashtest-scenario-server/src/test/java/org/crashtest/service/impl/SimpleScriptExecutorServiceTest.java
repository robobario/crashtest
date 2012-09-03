package org.crashtest.service.impl;

import org.crashtest.interpreter.ScriptExecutionException;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.impl.SimpleScriptExecutor;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.plan.SimpleExecutionPlan;
import org.crashtest.service.NoSuchScriptDefinedException;
import org.crashtest.service.ProgressReportProvider;
import org.crashtest.service.ScopeService;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.model.ScriptId;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleScriptExecutorServiceTest {

    @Test
    public void testSuccessfulExecution() throws ScriptExecutionException, NoSuchScriptDefinedException {
        ScriptRepositoryService repoService = Mockito.mock(ScriptRepositoryService.class);
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        Script script = Mockito.mock(Script.class);
        Mockito.when(repoService.getScript(Mockito.any(ScriptId.class))).thenReturn(script);
        ScriptExecutor scriptExecutor = Mockito.mock(SimpleScriptExecutor.class);
        SimpleExecutionPlan.Execution mock = Mockito.mock(SimpleExecutionPlan.Execution.class);
        ProgressReportProvider mockProgressProvider = Mockito.mock(ProgressReportProvider.class);
        Mockito.when(mock.getProgressReportProvider()).thenReturn(mockProgressProvider);
        Mockito.when(scriptExecutor.executeScript(Mockito.any(Script.class))).thenReturn(mock);
        Mockito.when(scopeService.getScriptExecutor()).thenReturn(scriptExecutor);
        SimpleScriptExecutorService service = new SimpleScriptExecutorService(scopeService, repoService);
        service.execute(Mockito.mock(ScriptId.class));
        Mockito.verify(scriptExecutor).executeScript(script);
    }

    @Test(expected = ScriptExecutionException.class)
    public void testNonExistentScript() throws ScriptExecutionException, NoSuchScriptDefinedException {
        ScriptRepositoryService repoService = Mockito.mock(ScriptRepositoryService.class);
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        Mockito.when(repoService.getScript(Mockito.any(ScriptId.class))).thenThrow(new NoSuchScriptDefinedException("fail"));
        SimpleScriptExecutorService service = new SimpleScriptExecutorService(scopeService, repoService);
        service.execute(Mockito.mock(ScriptId.class));
    }

    @Test(expected = ScriptExecutionException.class)
    public void testFailedExecution() throws ScriptExecutionException, NoSuchScriptDefinedException {
        ScriptRepositoryService repoService = Mockito.mock(ScriptRepositoryService.class);
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        Script script = Mockito.mock(Script.class);
        Mockito.when(repoService.getScript(Mockito.any(ScriptId.class))).thenReturn(script);
        ScriptExecutor scriptExecutor = Mockito.mock(SimpleScriptExecutor.class);
        Mockito.doThrow(new ScriptExecutionException("fail")).when(scriptExecutor).executeScript(Mockito.any(Script.class));
        Mockito.when(scopeService.getScriptExecutor()).thenReturn(scriptExecutor);
        SimpleScriptExecutorService service = new SimpleScriptExecutorService(scopeService, repoService);
        service.execute(Mockito.mock(ScriptId.class));
    }
}
