package org.crashtest.http.translation;

import com.google.common.collect.ImmutableList;
import org.crashtest.http.request.ParameterRequest;
import org.crashtest.http.request.RemoteMethodDefinitionRequest;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RemoteMethodDefinitionTranslatorTest {

    private static final String REMOTE_METHOD_NAME = "name";
    private static final String PARAMETER_NAME = "param";

    @Test
    public void testTranslate() throws TranslationException {
        RemoteMethodDefinitionTranslator translator = new RemoteMethodDefinitionTranslator();
        RemoteMethodDefinitionRequest toTranslate = new RemoteMethodDefinitionRequest();
        toTranslate.setName(REMOTE_METHOD_NAME);
        RemoteMethodDef translate = translator.translate(toTranslate);
        assertEquals(REMOTE_METHOD_NAME,translate.getName());
    }

    @Test
    public void testTranslate_WithParameter() throws TranslationException {
        RemoteMethodDefinitionTranslator translator = new RemoteMethodDefinitionTranslator();
        RemoteMethodDefinitionRequest toTranslate = new RemoteMethodDefinitionRequest();
        toTranslate.setName(REMOTE_METHOD_NAME);
        ParameterRequest parameterRequest = new ParameterRequest();
        parameterRequest.setName(PARAMETER_NAME);
        toTranslate.setParameters(ImmutableList.<ParameterRequest>of(parameterRequest));
        RemoteMethodDef translate = translator.translate(toTranslate);
        assertEquals(ImmutableList.of(ParameterDef.named(PARAMETER_NAME)),translate.getParameters());
    }
}
