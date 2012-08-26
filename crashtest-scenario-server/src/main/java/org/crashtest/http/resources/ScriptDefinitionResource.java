package org.crashtest.http.resources;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;
import org.crashtest.http.request.*;
import org.crashtest.http.request.expressions.IdentifierRequest;
import org.crashtest.http.request.expressions.LiteralRequest;
import org.crashtest.http.request.statements.MethodInvocationRequest;
import org.crashtest.http.request.statements.RemoteInvocationRequest;
import org.crashtest.http.response.ScriptDefinitionResponse;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.crashtest.service.ScriptRepositoryService;
import org.crashtest.service.impl.SimpleScriptRepositoryService;
import org.crashtest.service.model.ScriptId;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ScriptDefinitionResource extends ServerResource{
    private ObjectMapper mapper = new ObjectMapper();
    ScriptRepositoryService service = SimpleScriptRepositoryService.instance();

    @Post("json")
    public String define(InputStream document){
        ScriptDefinitionRequest definitionRequest;
        try {
            definitionRequest = mapper.readValue(new InputStreamReader(document), ScriptDefinitionRequest.class);
        } catch (IOException e) {
            return "fail - " + e;
        }
        if(!definitionRequest.isValid()){
            return "fail - invalid request";
        }
        Script.Builder script = Script.named(definitionRequest.getName());
        script.withStatements(Iterables.transform(definitionRequest.getStatementRequests(),new Function<StatementRequest, Statement>() {
            @Override
            public Statement apply(StatementRequest input) {
                final List<Statement> statementList = Lists.newArrayList();
                input.accept(new StatementRequestVisitor() {
                    @Override
                    public void visit(RemoteInvocationRequest request) {
                       statementList.add(RemoteInvocation.named(request.getName()).withParameterExpressions(Iterables.transform(request.getParameterExpressions(),expressionTransformer())).build());
                    }

                    @Override
                    public void visit(MethodInvocationRequest request) {
                        statementList.add(MethodInvocation.named(request.getName()).withParameterExpressions(Iterables.transform(request.getParameterExpressions(),expressionTransformer())).build());
                    }
                });
                return Iterables.getOnlyElement(statementList);
            }
        }));
        try {
            ScriptId scriptId = service.addScript(script.build());
            return mapper.writeValueAsString(ScriptDefinitionResponse.forId(scriptId));
        } catch (Exception e) {
            return "fail : " + e;
        }
    }

    private Function<ParameterExpressionRequest, Expression> expressionTransformer() {
        return new Function<ParameterExpressionRequest, Expression>() {
            @Override
            public Expression apply(ParameterExpressionRequest input) {
                final List<Expression> expressions = Lists.newArrayList();
                input.accept(new ExpressionRequestVisitor() {
                    @Override
                    public void visit(LiteralRequest request) {
                        expressions.add(Literal.of(request.getValue()));
                    }

                    @Override
                    public void visit(IdentifierRequest request) {
                        expressions.add(Identifier.named(request.getName()));
                    }
                });
                return Iterables.getOnlyElement(expressions);
            }
        };
    }
}
