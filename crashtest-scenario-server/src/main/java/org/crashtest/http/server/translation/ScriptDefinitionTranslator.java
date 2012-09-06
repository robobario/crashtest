package org.crashtest.http.server.translation;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.crashtest.http.server.request.*;
import org.crashtest.http.server.request.expressions.IdentifierRequest;
import org.crashtest.http.server.request.expressions.LiteralRequest;
import org.crashtest.http.server.request.statements.MethodInvocationRequest;
import org.crashtest.http.server.request.statements.RemoteInvocationRequest;
import org.crashtest.interpreter.model.Expression;
import org.crashtest.interpreter.model.Script;
import org.crashtest.interpreter.model.Statement;
import org.crashtest.interpreter.model.expressions.Identifier;
import org.crashtest.interpreter.model.expressions.Literal;
import org.crashtest.interpreter.model.statements.MethodInvocation;
import org.crashtest.interpreter.model.statements.RemoteInvocation;

import java.util.List;

public class ScriptDefinitionTranslator implements Translator<ScriptDefinitionRequest, Script> {
    private final static Function<ParameterExpressionRequest, Expression> EXPRESSION_TRANSFORMER = new Function<ParameterExpressionRequest, Expression>() {
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

    @Override
    public Script translate(ScriptDefinitionRequest toTranslate) throws TranslationException {
        try {
            Script.Builder script = Script.named(toTranslate.getName());
            script.withStatements(Iterables.transform(toTranslate.getStatementRequests(), new Function<StatementRequest, Statement>() {
                @Override
                public Statement apply(StatementRequest input) {
                    final List<Statement> statementList = Lists.newArrayList();
                    input.accept(new StatementRequestVisitor() {
                        @Override
                        public void visit(RemoteInvocationRequest request) {
                            statementList.add(RemoteInvocation.named(request.getName()).withParameterExpressions(Iterables.transform(request.getParameterExpressions(), EXPRESSION_TRANSFORMER)).build());
                        }

                        @Override
                        public void visit(MethodInvocationRequest request) {
                            statementList.add(MethodInvocation.named(request.getName()).withParameterExpressions(Iterables.transform(request.getParameterExpressions(), EXPRESSION_TRANSFORMER)).build());
                        }
                    });
                    return Iterables.getOnlyElement(statementList);
                }
            }));
            return script.build();
        } catch (Exception e) {
            throw new TranslationException(e);
        }
    }
}
