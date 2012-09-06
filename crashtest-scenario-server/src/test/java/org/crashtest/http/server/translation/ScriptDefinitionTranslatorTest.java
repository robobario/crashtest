package org.crashtest.http.server.translation;

import com.google.common.collect.ImmutableList;
import org.crashtest.http.server.request.ParameterExpressionRequest;
import org.crashtest.http.server.request.ScriptDefinitionRequest;
import org.crashtest.http.server.request.StatementRequest;
import org.crashtest.http.server.request.expressions.IdentifierRequest;
import org.crashtest.http.server.request.expressions.LiteralRequest;
import org.crashtest.http.server.request.statements.MethodInvocationRequest;
import org.crashtest.http.server.request.statements.RemoteInvocationRequest;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScriptDefinitionTranslatorTest {

    private static final String SCRIPT_NAME = "script";
    private static final String METHOD_NAME = "method";
    private static final String LITERAL_VALUE = "value";
    private static final String IDENTIFIER_NAME = "identifier";

    @Test
    public void testTranslate_Name() throws TranslationException {
        ScriptDefinitionTranslator translator = new ScriptDefinitionTranslator();
        ScriptDefinitionRequest toTranslate = new ScriptDefinitionRequest();
        toTranslate.setName(SCRIPT_NAME);
        Script translated = translator.translate(toTranslate);
        assertEquals(SCRIPT_NAME,translated.getName());
    }

    @Test
    public void testTranslate_MethodInvocation() throws TranslationException {
        ScriptDefinitionTranslator translator = new ScriptDefinitionTranslator();
        ScriptDefinitionRequest toTranslate = new ScriptDefinitionRequest();
        toTranslate.setName(SCRIPT_NAME);
        MethodInvocationRequest methodInvocation = new MethodInvocationRequest();
        methodInvocation.setName(METHOD_NAME);
        toTranslate.setStatementRequests(ImmutableList.<StatementRequest>of(methodInvocation));
        Script translated = translator.translate(toTranslate);
        assertEquals(ImmutableList.of(MethodInvocation.named(METHOD_NAME).build()),translated.getStatements());
    }

    @Test
    public void testTranslate_RemoteMethodInvocation() throws TranslationException {
        ScriptDefinitionTranslator translator = new ScriptDefinitionTranslator();
        ScriptDefinitionRequest toTranslate = new ScriptDefinitionRequest();
        toTranslate.setName(SCRIPT_NAME);
        RemoteInvocationRequest methodInvocation = new RemoteInvocationRequest();
        methodInvocation.setName(METHOD_NAME);
        toTranslate.setStatementRequests(ImmutableList.<StatementRequest>of(methodInvocation));
        Script translated = translator.translate(toTranslate);
        assertEquals(ImmutableList.of(RemoteInvocation.named(METHOD_NAME).build()),translated.getStatements());
    }

    @Test
    public void testTranslate_MethodInvocation_WithLiteral() throws TranslationException {
        ScriptDefinitionTranslator translator = new ScriptDefinitionTranslator();
        ScriptDefinitionRequest toTranslate = new ScriptDefinitionRequest();
        toTranslate.setName(SCRIPT_NAME);
        MethodInvocationRequest methodInvocation = new MethodInvocationRequest();
        methodInvocation.setName(METHOD_NAME);
        LiteralRequest element = new LiteralRequest();
        element.setValue(LITERAL_VALUE);
        methodInvocation.setParameterExpressions(ImmutableList.<ParameterExpressionRequest>of(element));
        toTranslate.setStatementRequests(ImmutableList.<StatementRequest>of(methodInvocation));
        Script translated = translator.translate(toTranslate);
        assertEquals(ImmutableList.of(MethodInvocation.named(METHOD_NAME).withParameterExpression(Literal.of(LITERAL_VALUE)).build()),translated.getStatements());
    }

    @Test
    public void testTranslate_RemoteMethodInvocation_WithLiteral() throws TranslationException {
        ScriptDefinitionTranslator translator = new ScriptDefinitionTranslator();
        ScriptDefinitionRequest toTranslate = new ScriptDefinitionRequest();
        toTranslate.setName(SCRIPT_NAME);
        RemoteInvocationRequest methodInvocation = new RemoteInvocationRequest();
        methodInvocation.setName(METHOD_NAME);
        LiteralRequest element = new LiteralRequest();
        element.setValue(LITERAL_VALUE);
        methodInvocation.setParameterExpressions(ImmutableList.<ParameterExpressionRequest>of(element));
        toTranslate.setStatementRequests(ImmutableList.<StatementRequest>of(methodInvocation));
        Script translated = translator.translate(toTranslate);
        assertEquals(ImmutableList.of(RemoteInvocation.named(METHOD_NAME).withParameterExpression(Literal.of(LITERAL_VALUE)).build()),translated.getStatements());
    }

    @Test
    public void testTranslate_MethodInvocation_WithIdentifier() throws TranslationException {
        ScriptDefinitionTranslator translator = new ScriptDefinitionTranslator();
        ScriptDefinitionRequest toTranslate = new ScriptDefinitionRequest();
        toTranslate.setName(SCRIPT_NAME);
        MethodInvocationRequest methodInvocation = new MethodInvocationRequest();
        methodInvocation.setName(METHOD_NAME);
        IdentifierRequest element = new IdentifierRequest();
        element.setName(IDENTIFIER_NAME);
        methodInvocation.setParameterExpressions(ImmutableList.<ParameterExpressionRequest>of(element));
        toTranslate.setStatementRequests(ImmutableList.<StatementRequest>of(methodInvocation));
        Script translated = translator.translate(toTranslate);
        assertEquals(ImmutableList.of(MethodInvocation.named(METHOD_NAME).withParameterExpression(Identifier.named(IDENTIFIER_NAME)).build()),translated.getStatements());
    }

    @Test
    public void testTranslate_RemoteMethodInvocation_WithIdentifier() throws TranslationException {
        ScriptDefinitionTranslator translator = new ScriptDefinitionTranslator();
        ScriptDefinitionRequest toTranslate = new ScriptDefinitionRequest();
        toTranslate.setName(SCRIPT_NAME);
        RemoteInvocationRequest methodInvocation = new RemoteInvocationRequest();
        methodInvocation.setName(METHOD_NAME);
        IdentifierRequest element = new IdentifierRequest();
        element.setName(IDENTIFIER_NAME);
        methodInvocation.setParameterExpressions(ImmutableList.<ParameterExpressionRequest>of(element));
        toTranslate.setStatementRequests(ImmutableList.<StatementRequest>of(methodInvocation));
        Script translated = translator.translate(toTranslate);
        assertEquals(ImmutableList.of(RemoteInvocation.named(METHOD_NAME).withParameterExpression(Identifier.named(IDENTIFIER_NAME)).build()),translated.getStatements());
    }
}
