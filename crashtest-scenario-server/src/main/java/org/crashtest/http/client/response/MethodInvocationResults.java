package org.crashtest.http.client.response;

import java.util.List;

public class MethodInvocationResults {
    private List<String> errorMessages;

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
