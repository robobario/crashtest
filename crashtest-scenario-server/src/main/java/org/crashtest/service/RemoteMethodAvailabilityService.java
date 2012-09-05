package org.crashtest.service;

import org.crashtest.service.model.RemoteMethodId;

public interface RemoteMethodAvailabilityService {
    boolean isRemoteMethodAvailable(RemoteMethodId id);
}
