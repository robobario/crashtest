package org.crashtest.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.crashtest.http.client.response.AvailableMethod;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.service.ScopeService;
import org.crashtest.service.model.NoSuchMethodDefinitionException;
import org.crashtest.service.model.RemoteMethodId;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SingleServerAvailabilityWorker {
    Map<RemoteMethodId, Boolean> availability = ImmutableMap.of();

    ScopeService service = SimpleScopeService.instance();

    SimpleRemoteMethodAvailabilityService availabilityService = SimpleRemoteMethodAvailabilityService.instance();

    public static SingleServerAvailabilityWorker getInstance() {
        return instance;
    }

    public static final SingleServerAvailabilityWorker instance = new SingleServerAvailabilityWorker();

    ObjectMapper mapper = new ObjectMapper();
    public SingleServerAvailabilityWorker() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Representation representation = new ClientResource("http://localhost:8182/crashtest/entry-points").get();
                    ImmutableMap.Builder<RemoteMethodId, Boolean> newAvailability = ImmutableMap.builder();
                    List<AvailableMethod> availableMethods = mapper.readValue(representation.getReader(), new TypeReference<List<AvailableMethod>>(){});
                    System.out.println(availableMethods);
                    List<RemoteMethodDef> remoteDefs = Lists.transform(availableMethods, AvailableMethod.TO_REMOTE_METHOD_DEF);
                    for(RemoteMethodDef remoteMethodDef : remoteDefs){
                        RemoteMethodId remoteMethodId;
                        try{
                            remoteMethodId = service.identify(remoteMethodDef);
                        }catch (NoSuchMethodDefinitionException e){
                            remoteMethodId = service.defineRemoteMethod(remoteMethodDef);
                        }
                        newAvailability.put(remoteMethodId,true);
                    }
                    availabilityService.replaceAvailability(newAvailability.build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask,0, TimeUnit.SECONDS.toMillis(10));
    }
}
