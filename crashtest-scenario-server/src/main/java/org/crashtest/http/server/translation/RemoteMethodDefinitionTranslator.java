package org.crashtest.http.server.translation;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import org.crashtest.http.server.request.ParameterRequest;
import org.crashtest.http.server.request.RemoteMethodDefinitionRequest;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.RemoteMethodDef;

public class RemoteMethodDefinitionTranslator implements Translator<RemoteMethodDefinitionRequest, RemoteMethodDef> {
    @Override
    public RemoteMethodDef translate(RemoteMethodDefinitionRequest toTranslate) throws TranslationException {
        try {
            RemoteMethodDef.Builder definition = RemoteMethodDef.named(toTranslate.getName());
            definition.withParameterDefs(Iterables.transform(toTranslate.getParameters(), new Function<ParameterRequest, ParameterDef>() {
                @Override
                public ParameterDef apply(ParameterRequest input) {
                    return ParameterDef.named(input.getName());
                }
            }));
            return definition.build();
        } catch (Exception e) {
            throw new TranslationException(e);
        }
    }
}
