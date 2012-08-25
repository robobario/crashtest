package org.crashtest.service;

import com.google.common.collect.ImmutableList;
import org.crashtest.request.EntryPointInvocation;
import org.crashtest.request.ParameterInvocation;
import org.crashtest.response.EntryPoint;
import org.crashtest.response.Parameter;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClasspathScanningEntryPointServiceTest {

    @Test
    public void testFindingEntryPoints(){
        EntryPointService entryPointService = ClasspathScanningEntryPointService.newInstance();
        Collection<EntryPoint> allEntryPoints = entryPointService.getAllEntryPoints();
        assertTrue("there should be an entry point named do test",allEntryPoints.contains(EntryPoint.named("do test").build()));
    }

    @Test
    public void testInvokingEntryPoints(){
        EntryPointService entryPointService = ClasspathScanningEntryPointService.newInstance();
        EntryPointInvocation entryPointInvocation = new EntryPointInvocation();
        entryPointInvocation.setEntryPointName("do test");
        try {
            entryPointService.invoke(entryPointInvocation);
        } catch (NonExistentEntryPointException e) {
            fail("the entry point did not exist");
        } catch (EntryPointInvocationException e) {
            fail("the invocation failed " + e.toString());
        }
    }

    @Test(expected = EntryPointInvocationException.class)
    public void testInvokingWithTooManyArguments() throws EntryPointInvocationException, NonExistentEntryPointException {
        EntryPointService entryPointService = ClasspathScanningEntryPointService.newInstance();
        EntryPointInvocation entryPointInvocation = new EntryPointInvocation();
        entryPointInvocation.setEntryPointName("do test");
        ParameterInvocation parameterInvocation = new ParameterInvocation();
        parameterInvocation.setName("bad name");
        parameterInvocation.setValue("bad value");
        entryPointInvocation.setParameters(ImmutableList.of(parameterInvocation));
        entryPointService.invoke(entryPointInvocation);
    }

    @Test(expected = NonExistentEntryPointException.class)
    public void testInvokingNonExistentEntryPoints() throws EntryPointInvocationException, NonExistentEntryPointException {
        EntryPointService entryPointService = ClasspathScanningEntryPointService.newInstance();
        EntryPointInvocation entryPointInvocation = new EntryPointInvocation();
        entryPointInvocation.setEntryPointName("non exist!");
        entryPointService.invoke(entryPointInvocation);
    }

    @Test
    public void testInvokingWithArguments() throws EntryPointInvocationException, NonExistentEntryPointException {
        EntryPointService entryPointService = ClasspathScanningEntryPointService.newInstance();
        EntryPointInvocation entryPointInvocation = new EntryPointInvocation();
        entryPointInvocation.setEntryPointName("do tests");
        ParameterInvocation parameterInvocation = new ParameterInvocation();
        parameterInvocation.setName("var");
        parameterInvocation.setValue("good value");
        entryPointInvocation.setParameters(ImmutableList.of(parameterInvocation));
        entryPointService.invoke(entryPointInvocation);
    }

    @Test(expected = EntryPointInvocationException.class)
    public void testInvokingWithNotEnoughArguments() throws EntryPointInvocationException, NonExistentEntryPointException {
        EntryPointService entryPointService = ClasspathScanningEntryPointService.newInstance();
        EntryPointInvocation entryPointInvocation = new EntryPointInvocation();
        entryPointInvocation.setEntryPointName("do tests");
        entryPointInvocation.setParameters(ImmutableList.of(new ParameterInvocation()));
        entryPointService.invoke(entryPointInvocation);
    }

    @Test
    public void testFindingEntryPointsWithVariables(){
        EntryPointService entryPointService = ClasspathScanningEntryPointService.newInstance();
        Collection<EntryPoint> allEntryPoints = entryPointService.getAllEntryPoints();
        assertTrue("there should be an entry point named do test",allEntryPoints.contains(EntryPoint.named("do tests").withParameter(Parameter.named("var")).build()));
    }
}
