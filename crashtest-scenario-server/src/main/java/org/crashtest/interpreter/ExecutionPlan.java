package org.crashtest.interpreter;

import org.crashtest.interpreter.model.plan.ProgressAwareExecutionTreeWalker;
import org.crashtest.interpreter.model.plan.SimpleExecutionPlan;
import org.crashtest.service.RemoteInvokerService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface ExecutionPlan {
     SimpleExecutionPlan.Execution execute(RemoteInvokerService service, ExecutorService executorService);
}
