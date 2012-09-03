package org.crashtest.interpreter.model.plan;

import org.apache.commons.lang.NotImplementedException;
import org.crashtest.interpreter.model.plan.descriptions.MethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.RemoteMethodInvocationStep;
import org.crashtest.interpreter.model.plan.descriptions.ScriptInvocationStep;

public class SimpleExecutionTree implements ExecutionTree {
    private InvocationStep rootStep;

    public SimpleExecutionTree(InvocationStep rootStep) {
        this.rootStep = rootStep;
    }

    public static Builder builderForScriptNamed(String scriptName){
        return new Builder(scriptName);
    }

    @Override
    public ProgressAwareExecutionTreeWalker getNewProgressAwareTreeWalker() {
        return ProgressAwareExecutionTreeWalker.decorate(rootStep);
    }

    public static class Builder {
        InvocationStep currentStep;
        InvocationStep firstStep;

        private Builder(String scriptNamed){
            currentStep = ScriptInvocationStep.named(scriptNamed).build();
            firstStep = currentStep;
        }


        public Builder pushMethod(final MethodInvocationStep method){
            this.currentStep.accept(new InvocationStepVisitor() {
                @Override
                public void visit(MethodInvocationStep step) {
                    step.addChild(method);
                    method.setParent(step);
                    currentStep = method;
                }

                @Override
                public void visit(RemoteMethodInvocationStep step) {
                    throw new NotImplementedException("the current node should never be a remote method as it is a leaf");
                }

                @Override
                public void visit(ScriptInvocationStep step) {
                    step.addChild(method);
                    method.setParent(step);
                    currentStep = method;
                }
            });
           return this;
        }

        public Builder pushRemote(final RemoteMethodInvocationStep method){
            this.currentStep.accept(new InvocationStepVisitor() {
                @Override
                public void visit(MethodInvocationStep step) {
                    step.addChild(method);
                    method.setParent(step);
                    currentStep = method;
                }

                @Override
                public void visit(RemoteMethodInvocationStep step) {
                    throw new NotImplementedException("the current node should never be a remote method as it is a leaf");
                }

                @Override
                public void visit(ScriptInvocationStep step) {
                    step.addChild(method);
                    method.setParent(step);
                    currentStep = method;
                }
            });
            return this;
        }

        public Builder pop(){
            this.currentStep.accept(new InvocationStepVisitor() {
                @Override
                public void visit(MethodInvocationStep step) {
                    currentStep = step.getParent();
                }

                @Override
                public void visit(RemoteMethodInvocationStep step) {
                    currentStep = step.getParent();
                }

                @Override
                public void visit(ScriptInvocationStep step) {
                    throw new NotImplementedException("don't try to pop the root node");
                }
            });
            return this;
        }

        public ExecutionTree build(){
            if(!firstStep.equals(currentStep)){
                throw new RuntimeException("did not create a well formed execution tree");
            }
            return new SimpleExecutionTree(firstStep);
        }

    }

}
