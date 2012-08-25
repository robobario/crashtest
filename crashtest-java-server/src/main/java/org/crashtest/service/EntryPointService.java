package org.crashtest.service;

import org.crashtest.request.EntryPointInvocation;
import org.crashtest.response.EntryPoint;

import java.util.Collection;

public interface EntryPointService {
    public Collection<EntryPoint> getAllEntryPoints();

    void invoke(EntryPointInvocation entryPointInvocation) throws NonExistentEntryPointException, EntryPointInvocationException;
}
