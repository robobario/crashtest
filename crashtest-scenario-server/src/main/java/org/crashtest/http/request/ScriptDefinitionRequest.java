package org.crashtest.http.request;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

public class ScriptDefinitionRequest implements Request{

    private String name;
    private List<StatementRequest> statementRequests = ImmutableList.of();

    public ScriptDefinitionRequest() {
    }

    public String getName() {
        return name;
    }

    public List<StatementRequest> getStatementRequests() {
        return statementRequests;
    }

    public void setStatementRequests(List<StatementRequest> statementRequests) {
        this.statementRequests = statementRequests;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(name) && Iterables.all(statementRequests,Requests.IS_VALID);
    }
}
