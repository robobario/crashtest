package org.crashtest.service;

import org.crashtest.annotations.Entry;
import org.crashtest.annotations.Fixture;

@Fixture
public class Test {
    @Entry("hi")
    public void test(){System.out.println("hi");}
}
