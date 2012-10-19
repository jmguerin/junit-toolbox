package com.googlecode.junittoolbox;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import static org.junit.Assert.*;

public class ParallelSuiteTest {

    private static volatile Thread thread1 = null;
    private static volatile Thread thread2 = null;

    @RunWith(ParallelSuite.class)
    @SuiteClasses({})
    public static class EmptyExample{}

    @Test
    public void test_empty_ParallelSuite() {
        Result result = JUnitCore.runClasses(EmptyExample.class);
        assertTrue(result.wasSuccessful());
        assertEquals(0, result.getRunCount());
    }

    public static class Test1 {
        @Test
        public void test() {
            thread1 = Thread.currentThread();
        }
    }

    public static class Test2 {
        @Test
        public void test() {
            thread2 = Thread.currentThread();
        }
    }

    @RunWith(ParallelSuite.class)
    @SuiteClasses({ Test1.class, Test2.class })
    public static class Example1 {}

    @Test
    public void test() {
        Result result = JUnitCore.runClasses(Example1.class);
        assertTrue(result.wasSuccessful());
        assertEquals(2, result.getRunCount());
        assertNotNull(thread1);
        assertNotNull(thread2);
        assertNotSame(thread1, thread2);
    }

    @RunWith(ParallelSuite.class)
    @SuiteClasses({ Test1.class, Test2.class, ParallelRunnerTest.Example.class })
    public static class Example2 {}

    @Test
    public void test_with_child_using_ParallelRunner() {
        Result result = JUnitCore.runClasses(Example2.class);
        assertTrue(result.wasSuccessful());
        assertEquals(4, result.getRunCount());
        assertNotSame(ParallelRunnerTest.thread1, ParallelRunnerTest.thread2);
    }

    @RunWith(ParallelSuite.class)
    @SuiteClasses({ Example1.class, Example2.class })
    public static class Example3 {}

    @Test
    public void test_nested_ParallelSuites() {
        Result result = JUnitCore.runClasses(Example3.class);
        assertTrue(result.wasSuccessful());
        assertEquals(6, result.getRunCount());
    }
}
