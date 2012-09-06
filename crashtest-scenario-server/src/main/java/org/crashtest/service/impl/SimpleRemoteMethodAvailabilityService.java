package org.crashtest.service.impl;

import com.google.common.collect.ImmutableMap;
import org.crashtest.service.RemoteMethodAvailabilityService;
import org.crashtest.service.model.RemoteMethodId;

import java.util.Map;

public class SimpleRemoteMethodAvailabilityService implements RemoteMethodAvailabilityService {

    Map<RemoteMethodId, Boolean> availability = ImmutableMap.of();

    public static final SimpleRemoteMethodAvailabilityService instance = new SimpleRemoteMethodAvailabilityService();

    private SimpleRemoteMethodAvailabilityService() {
    }

    public static SimpleRemoteMethodAvailabilityService instance(){
        return instance;
    }

    @Override
    public boolean isRemoteMethodAvailable(RemoteMethodId id) {
        return availability.containsKey(id) && availability.get(id);
    }

    public void replaceAvailability(ImmutableMap<RemoteMethodId, Boolean> availability) {
        this.availability = availability;
    }
}
