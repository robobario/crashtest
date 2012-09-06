package org.crashtest.http.server.translation;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.crashtest.http.server.request.*;
import org.crashtest.http.server.request.expressions.IdentifierRequest;
import org.crashtest.http.server.request.expressions.LiteralRequest;
import org.crashtest.http.server.request.statements.MethodInvocationRequest;
import org.crashtest.http.server.request.statements.RemoteInvocationRequest;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.MethodDef;
import org.crashtest.interpreter.model.ParameterDef;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;

import java.util.List;

public class MethodDefinitionTranslator implements Translator<MethodDefinitionRequest, MethodDef> {

    @Override
    public MethodDef translate(MethodDefinitionRequest toTranslate) throws TranslationException {
        String name = toTranslate.getName();
        if (Strings.isNullOrEmpty(name)) {
            throw new TranslationException("translation failed because the method description had no named");
        }
        MethodDef.Builder definition = MethodDef.named(name);
        definition.withParameters(Iterables.transform(toTranslate.getParameters(), PARAMETER_TRANSLATOR));
        definition.withStatements(Iterables.transform(toTranslate.getStatements(), STATEMENT_TRANSLATOR));
        return definition.build();
    }

    private static final Function<ParameterRequest, ParameterDef> PARAMETER_TRANSLATOR = new Function<ParameterRequest, ParameterDef>() {
        @Override
        public ParameterDef apply(ParameterRequest input) {
            return ParameterDef.named(input.getName());
        }
    };


    private static final Function<ParameterExpressionRequest, Expression> EXPRESSION_TRANSFORMER = new Function<ParameterExpressionRequest, Expression>() {
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

    private static final Function<StatementRequest, Statement> STATEMENT_TRANSLATOR = new Function<StatementRequest, Statement>() {
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
    };


    private static Iterable<Expression> getParameterExpressionsForMethodInvocation(MethodInvocationRequest request) {
        return Iterables.transform(request.getParameterExpressions(), EXPRESSION_TRANSFORMER);
    }

    private static Iterable<Expression> getParameterExpressionsForRemoteInvocation(RemoteInvocationRequest request) {
        return Iterables.transform(request.getParameterExpressions(), EXPRESSION_TRANSFORMER);
    }


}
