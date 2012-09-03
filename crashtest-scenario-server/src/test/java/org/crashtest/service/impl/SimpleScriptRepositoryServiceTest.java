package org.crashtest.service.impl;

import com.google.common.collect.ImmutableList;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.service.NoSuchScriptDefinedException;
import org.crashtest.service.ScopeService;
import org.crashtest.service.ScriptDefinitionException;
import org.crashtest.service.model.ScriptId;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleScriptRepositoryServiceTest {

    private static final String SCRIPT_NAME = "named";

    @Test(expected = ScriptDefinitionException.class)
    public void testAddScriptWithNoName() throws ScriptDefinitionException {
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        SimpleScriptRepositoryService service = new SimpleScriptRepositoryService(scopeService);
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getStatements()).thenReturn(ImmutableList.<Statement>of());
        service.addScript(mockScript);
    }

    @Test
    public void testAddScriptWithName() throws ScriptDefinitionException {
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        SimpleScriptRepositoryService service = new SimpleScriptRepositoryService(scopeService);
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getName()).thenReturn(SCRIPT_NAME);
        Mockito.when(mockScript.getStatements()).thenReturn(ImmutableList.<Statement>of());
        ScriptId scriptId = service.addScript(mockScript);
        assertNotNull(scriptId);
    }

    @Test(expected = ScriptDefinitionException.class)
    public void testAddScriptWithInvalidStatement() throws ScriptDefinitionException {
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        SimpleScriptRepositoryService service = new SimpleScriptRepositoryService(scopeService);
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getName()).thenReturn(SCRIPT_NAME);
        Mockito.when(mockScript.getStatements()).thenReturn(ImmutableList.<Statement>of(Mockito.mock(Statement.class)));
        Mockito.when(scopeService.isStatementDefined(Mockito.any(Statement.class))).thenReturn(false);
        service.addScript(mockScript);
    }

    @Test
    public void testAddScriptWithValidStatement() throws ScriptDefinitionException {
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        SimpleScriptRepositoryService service = new SimpleScriptRepositoryService(scopeService);
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getName()).thenReturn(SCRIPT_NAME);
        Mockito.when(mockScript.getStatements()).thenReturn(ImmutableList.<Statement>of(Mockito.mock(Statement.class)));
        Mockito.when(scopeService.isStatementDefined(Mockito.any(Statement.class))).thenReturn(true);
        ScriptId scriptId = service.addScript(mockScript);
        assertNotNull(scriptId);
    }


    @Test(expected = ScriptDefinitionException.class)
    public void testAddingDuplicateScript() throws ScriptDefinitionException {
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        SimpleScriptRepositoryService service = new SimpleScriptRepositoryService(scopeService);
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getName()).thenReturn(SCRIPT_NAME);
        Mockito.when(mockScript.getStatements()).thenReturn(ImmutableList.<Statement>of());
        service.addScript(mockScript);
        service.addScript(mockScript);
    }

    @Test
    public void testGetScript() throws ScriptDefinitionException, NoSuchScriptDefinedException {
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        SimpleScriptRepositoryService service = new SimpleScriptRepositoryService(scopeService);
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getName()).thenReturn(SCRIPT_NAME);
        Mockito.when(mockScript.getStatements()).thenReturn(ImmutableList.<Statement>of());
        Script script = service.getScript(service.addScript(mockScript));
        assertEquals(mockScript,script);
    }

    @Test(expected = NoSuchScriptDefinedException.class)
    public void testGetNonExistentScript() throws ScriptDefinitionException, NoSuchScriptDefinedException {
        ScopeService scopeService = Mockito.mock(ScopeService.class);
        SimpleScriptRepositoryService service = new SimpleScriptRepositoryService(scopeService);
        Script mockScript = Mockito.mock(Script.class);
        Mockito.when(mockScript.getName()).thenReturn(SCRIPT_NAME);
        Mockito.when(mockScript.getStatements()).thenReturn(ImmutableList.<Statement>of());
        ScriptId scriptId = service.addScript(mockScript);
        service.getScript(ScriptId.of(scriptId.getId()+1));
    }
}
