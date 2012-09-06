package org.crashtest.service.impl;

import com.google.common.collect.ImmutableList;
import org.codehaus.jackson.map.ObjectMapper;
import org.crashtest.http.client.request.RemoteMethodInvocation;
import org.crashtest.http.client.response.MethodInvocationResults;
import org.crashtest.service.RemoteInvokerService;
import org.crashtest.service.model.RemoteInvocationDescription;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import java.io.Reader;
import java.util.List;

public class SingleServerRemoteInvokerService implements RemoteInvokerService{
    ObjectMapper mapper = new ObjectMapper();

    private static final SingleServerRemoteInvokerService instance = new SingleServerRemoteInvokerService();

    private SingleServerRemoteInvokerService() {
    }

    @Override
    public List<ErrorMessage> invoke(RemoteInvocationDescription description) {
        RemoteMethodInvocation remoteMethodInvocation = RemoteMethodInvocation.transform(description);
        try {
            Reader reader = new ClientResource("http://localhost:8182/crashtest/entry-points").post(remoteMethodInvocation, MediaType.APPLICATION_JSON).getReader();
            MethodInvocationResults methodInvocationResults = mapper.readValue(reader, MethodInvocationResults.class);
            return ErrorMessage.fromStrings(methodInvocationResults.getErrorMessages());
        }catch (Exception e){
            return ImmutableList.of(ErrorMessage.withMessage(e.getMessage()));
        }
    }

    public static SingleServerRemoteInvokerService instance(){
        return instance;
    }
}
