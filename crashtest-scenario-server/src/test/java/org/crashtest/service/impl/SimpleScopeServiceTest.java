package org.crashtest.service.impl;

import org.apache.commons.lang.reflect.FieldUtils;
import org.crashtest.interpreter.MethodDefinitionException;
import org.crashtest.interpreter.Scope;
import org.crashtest.interpreter.ScriptExecutor;
import org.crashtest.interpreter.impl.SimpleScriptExecutor;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.service.model.MethodId;
import org.crashtest.service.model.NoSuchMethodDefinitionException;
import org.crashtest.service.model.RemoteMethodId;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimpleScopeServiceTest {

    @Test
    public void testDefiningMethod() throws MethodDefinitionException {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        MethodDef mockDef = Mockito.mock(MethodDef.class);
        Mockito.when(mockScope.addMethodDef(Mockito.any(MethodDef.class))).thenReturn(mockDef);
        MethodId methodId = simpleScopeService.defineMethod(mockDef);
        assertNotNull(methodId);
    }

    @Test(expected = MethodDefinitionException.class)
    public void testExceptionOnAttemptToDefineDuplicateMethod() throws MethodDefinitionException {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        MethodDef mockDef = Mockito.mock(MethodDef.class);
        Mockito.when(mockScope.addMethodDef(Mockito.any(MethodDef.class))).thenThrow(new MethodDefinitionException("fail"));
        simpleScopeService.defineMethod(mockDef);
    }


    @Test
    public void testRetrievingMethodDef() throws MethodDefinitionException, NoSuchMethodDefinitionException {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        MethodDef mockDef = Mockito.mock(MethodDef.class);
        Mockito.when(mockScope.addMethodDef(Mockito.any(MethodDef.class))).thenReturn(mockDef);
        MethodId methodId = simpleScopeService.defineMethod(mockDef);
        MethodDef methodDef = simpleScopeService.getMethodDef(methodId);
        assertEquals(mockDef,methodDef);
    }

    @Test(expected = MethodDefinitionException.class)
    public void testExceptionOnAttemptToDefineDuplicateRemoteMethod() throws MethodDefinitionException {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        RemoteMethodDef mockDef = Mockito.mock(RemoteMethodDef.class);
        Mockito.when(mockScope.addRemoteMethodDef(Mockito.any(RemoteMethodDef.class))).thenThrow(new MethodDefinitionException("fail"));
        simpleScopeService.defineRemoteMethod(mockDef);
    }

    @Test
    public void testDefiningRemoteMethod() throws MethodDefinitionException {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        RemoteMethodDef mockDef = Mockito.mock(RemoteMethodDef.class);
        Mockito.when(mockScope.addRemoteMethodDef(Mockito.any(RemoteMethodDef.class))).thenReturn(mockDef);
        RemoteMethodId methodId = simpleScopeService.defineRemoteMethod(mockDef);
        assertNotNull(methodId);
    }

    @Test
    public void testRetrievingRemoteMethodDef() throws MethodDefinitionException, NoSuchMethodDefinitionException {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        RemoteMethodDef mockDef = Mockito.mock(RemoteMethodDef.class);
        Mockito.when(mockScope.addRemoteMethodDef(Mockito.any(RemoteMethodDef.class))).thenReturn(mockDef);
        RemoteMethodId methodId = simpleScopeService.defineRemoteMethod(mockDef);
        RemoteMethodDef methodDef = simpleScopeService.getRemoteMethodDef(methodId);
        assertEquals(mockDef,methodDef);
    }

    @Test
    public void testIsStatementDefined() {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        Mockito.when(mockScope.isStatementDefined(Mockito.any(Statement.class))).thenReturn(true);
        boolean statementDefined = simpleScopeService.isStatementDefined(Mockito.mock(Statement.class));
        assertTrue(statementDefined);
    }

    @Test
    public void testGetScriptExecutorHasACopiedScope() throws IllegalAccessException {
        Scope mockScope = Mockito.mock(Scope.class);
        SimpleScopeService simpleScopeService = new SimpleScopeService(mockScope);
        Mockito.when(mockScope.copy()).thenReturn(mockScope);
        ScriptExecutor scriptExecutor = simpleScopeService.getScriptExecutor();
        Field field = FieldUtils.getField(SimpleScriptExecutor.class, "scope", true);
        field.setAccessible(true);
        assertEquals(mockScope, field.get(scriptExecutor));
    }
}
