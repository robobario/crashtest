package org.crashtest.interpreter.model.plan;

import com.google.common.collect.ImmutableList;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.crashtest.common.model.ParameterDescription;
import org.crashtest.interpreter.model.plan.descriptions.MethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.RemoteMethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.ScriptInvocationStep;
import org.crashtest.service.ProgressReportProvider;
import org.crashtest.service.model.ProgressTreeNode;

import java.util.List;

public class ProgressAwareExecutionTreeWalker implements ExecutionTreeWalker, ProgressReportProvider, ProgressTreeNode {
    private InvocationStep decorated;
    List<ProgressAwareExecutionTreeWalker> children = ImmutableList.of();
    private boolean completed = false;

    private ProgressAwareExecutionTreeWalker(InvocationStep decorated) {
        this.decorated = decorated;
    }

    @Override
    @JsonIgnore
    public ProgressTreeNode getProgressTree() {
        return this;
    }

    public static ProgressAwareExecutionTreeWalker decorate(final InvocationStep step) {
        ProgressAwareExecutionTreeWalker rootStep = new ProgressAwareExecutionTreeWalker(step);
        processInvocation(step, rootStep);
        return rootStep;
    }

    private static void processInvocation(InvocationStep step, final ProgressAwareExecutionTreeWalker currentLevel) {
        step.accept(new InvocationStepVisitor() {
            @Override
            public void visit(MethodInvocationStep step) {
                for (InvocationStep invocationStep : step.getInvocationList()) {
                    ProgressAwareExecutionTreeWalker nextLevel = new ProgressAwareExecutionTreeWalker(invocationStep);
                    currentLevel.addChild(nextLevel);
                    processInvocation(invocationStep, nextLevel);
                }
            }

            @Override
            public void visit(RemoteMethodInvocationStep step) {
                //Do nothing as we are at a leaf
            }

            @Override
            public void visit(ScriptInvocationStep step) {
                for (InvocationStep invocationStep : step.getInvocationList()) {
                    ProgressAwareExecutionTreeWalker nextLevel = new ProgressAwareExecutionTreeWalker(invocationStep);
                    currentLevel.addChild(nextLevel);
                    processInvocation(invocationStep, nextLevel);
                }
            }
        });
    }


    @Override
    public void depthFirst(InvocationStepVisitor visitor) {
        System.out.println("beginning execution of " + getDecorated().getName());
        for (ExecutionTreeWalker decorator : children) {
            decorator.depthFirst(visitor);
        }
        getDecorated().accept(visitor);
        System.out.println("finished execution of " + getDecorated().getName());
        completed = true;
    }

    public void addChild(ProgressAwareExecutionTreeWalker nextLevel) {
        children = ImmutableList.<ProgressAwareExecutionTreeWalker>builder().addAll(children).add(nextLevel).build();
    }

    @JsonIgnore
    public InvocationStep getDecorated() {
        return decorated;
    }

    @Override
    public List<ProgressTreeNode> getChildren() {
        return ImmutableList.<ProgressTreeNode>copyOf(children);
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public String getName() {
        return decorated.getName();
    }

    @Override
    public List<ParameterDescription> getParameters() {
        return decorated.getParameters();
    }

    @Override
    public List<Exception> getExceptions() {
        return ImmutableList.of();
    }
}
