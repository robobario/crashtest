package org.crashtest.service.impl;

import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.crashtest.interpreter.model.RemoteMethodDef;
import org.crashtest.service.RemoteMethodAvailabilityService;
import org.crashtest.service.ScopeService;
import org.crashtest.service.model.NoSuchMethodDefinitionException;
import org.crashtest.service.model.RemoteMethodId;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SingleServerRemoteMethodAvailabilityService implements RemoteMethodAvailabilityService {

    Map<RemoteMethodId, Boolean> availability = ImmutableMap.of();

    ScopeService service = SimpleScopeService.instance();

    public static SingleServerRemoteMethodAvailabilityService instance = new SingleServerRemoteMethodAvailabilityService();

    ObjectMapper mapper = new ObjectMapper();

    private SingleServerRemoteMethodAvailabilityService() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Representation representation = new ClientResource("http://localhost:8182/crashtest/entry-points").get();

                    AvailableMethods availableMethods = mapper.readValue(representation.getReader(), AvailableMethods.class);
                    for(RemoteMethodId id: service.getAllRemoteMethodIds()){
                        RemoteMethodDef def = service.getRemoteMethodDef(id);
                        //convert to an available method for comparison
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodDefinitionException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0, TimeUnit.SECONDS.toMillis(10));
    }

    public static SingleServerRemoteMethodAvailabilityService instance(){
        return instance;
    }

    @Override
    public boolean isRemoteMethodAvailable(RemoteMethodId id) {
        return availability.containsKey(id) && availability.get(id);
    }

    private class AvailableMethods {
        public List<AvailableMethod> getMethod() {
            return method;
        }

        public void setMethod(List<AvailableMethod> method) {
            this.method = method;
        }

        private List<AvailableMethod> method;
    }

    private class AvailableMethod {

        private List<AvailableParameters> parameters;

        private String name;

        public List<AvailableParameters> getParameters() {
            return parameters;
        }

        public void setParameters(List<AvailableParameters> parameters) {
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    private class AvailableParameters {
        private String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String name;
    }
}
