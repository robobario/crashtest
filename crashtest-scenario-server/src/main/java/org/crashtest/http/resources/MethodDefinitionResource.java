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
import org.crashtest.http.response.MethodDefinitionResponse;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;
import org.crashtest.service.ScopeService;
import org.crashtest.service.impl.SimpleScopeService;
import org.crashtest.service.model.MethodId;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MethodDefinitionResource extends ServerResource {

    private ObjectMapper mapper = new ObjectMapper();

    ScopeService service = SimpleScopeService.instance();

    @Post("json")
    public String define(InputStream document){
        MethodDefinitionRequest definitionRequest;
        try {
            definitionRequest = mapper.readValue(new InputStreamReader(document), MethodDefinitionRequest.class);
        } catch (Exception e) {
            return "fail : " + e;
        }
        if(!definitionRequest.isValid()){
            return "fail - invalid request";
        }
        MethodDef.Builder definition = MethodDef.named(definitionRequest.getName());
        definition.withParameters(Iterables.transform(definitionRequest.getParameters(),new Function<ParameterRequest, ParameterDef>() {
            @Override
            public ParameterDef apply(ParameterRequest input) {
                return ParameterDef.named(input.getName());
            }
        }));
        definition.withStatements(Iterables.transform(definitionRequest.getStatements(), new Function<StatementRequest, Statement>() {
            @Override
            public Statement apply(StatementRequest input) {
                final List<Statement> statementList = Lists.newArrayList();
                input.accept(new StatementRequestVisitor() {
                    @Override
                    public void visit(RemoteInvocationRequest request) {
                        Iterable<Expression> transform = getParameterExpressionsForRemoteInvocation(request);
                        statementList.add(RemoteInvocation.named(request.getName()).withParameterExpressions(transform).build());
                    }

                    @Override
                    public void visit(MethodInvocationRequest request) {
                        Iterable<Expression> transform = getParameterExpressionsForMethodInvocation(request);
                        statementList.add(MethodInvocation.named(request.getName()).withParameterExpressions(transform).build());
                    }
                });
                return Iterables.getOnlyElement(statementList);
            }
        }));
        try {
            MethodId methodId = service.defineMethod(definition.build());
            return mapper.writeValueAsString(MethodDefinitionResponse.forId(methodId));
        } catch (Exception e) {
            return "fail - " + e;
        }
    }

    private Iterable<Expression> getParameterExpressionsForMethodInvocation(MethodInvocationRequest request) {
        return Iterables.transform(request.getParameterExpressions(), expressionTransformer());
    }

    private Iterable<Expression> getParameterExpressionsForRemoteInvocation(RemoteInvocationRequest request) {
        return Iterables.transform(request.getParameterExpressions(), expressionTransformer());
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
