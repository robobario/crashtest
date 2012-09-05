package org.crashtest.http.response;

import com.google.common.collect.ImmutableList;
import org.crashtest.service.model.ProgressTreeNode;

import java.util.List;

public class ExecutionProgressResponse implements Response {
    private ProgressTreeNode root;

    private ExecutionProgressResponse(ProgressTreeNode root) {
        this.root = root;
    }

    public ProgressTreeNode getProgressTree(){
        return root;
    }

    @Override
    public List<String> getErrors() {
        return ImmutableList.of();
    }

    public static ExecutionProgressResponse forProgressTree(ProgressTreeNode root){
       return new ExecutionProgressResponse(root);
    }
}
