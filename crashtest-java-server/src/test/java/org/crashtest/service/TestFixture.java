package org.crashtest.service;

import org.crashtest.annotations.Entry;
import org.crashtest.annotations.Fixture;

@Fixture
public class TestFixture {

    public TestFixture(){
    }

    @Entry("do test")
    public void doTest(){
        //do the thing!
    }

    @Entry("do tests")
    public void doTestThing(@Entry("var")String var){
        //do the thing!
    }
}
