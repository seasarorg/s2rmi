/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.remoting.rmi.adaptor.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.remoting.rmi.adaptor.impl.RMIAdaptorImpl;
import org.seasar.remoting.rmi.filter.RMIFilter;
import org.seasar.remoting.rmi.filter.RMIFilterChain;

/**
 * @author murata
 */
public class RMIAdaptorImplTest extends S2TestCase {

    private static String PATH = "test.dicon";

    // 変数の自動セット
    private RMIAdaptorImpl adaptor;

    public void testInvoke() throws Exception {
        assertEquals("Hello!", adaptor.invoke("hello", "say", null));
    }

    public void testInvoke1() throws Exception {
        try {
            adaptor.invoke("hello1", "say", null);
            fail();
        }
        catch (ComponentNotFoundRuntimeException expected) {
        }
    }

    public void testInvoke2() throws Exception {
        try {
            adaptor.invoke("hello2", "say", null);
            fail();
        }
        catch (TooManyRegistrationRuntimeException expected) {
        }
    }

    public void testInvoke3() throws Exception {
        try {
            adaptor.invoke("hello", "hello", null);
            fail();
        }
        catch (MethodNotFoundRuntimeException expected) {
        }
    }

    public void testInvoke4() throws Exception {
        try {
            String[] args = { "hello" };
            adaptor.invoke("hello", "say", args);
            fail();
        }
        catch (MethodNotFoundRuntimeException expected) {
        }
    }

    public void testInvoke5() throws Exception {
        adaptor.setInvoker(null);
        try {
            adaptor.invoke("hello", "say", null);
            fail();
        }
        catch (ComponentNotFoundRuntimeException expected) {
        }
    }

    public void testFilter1() throws Exception {
        adaptor.addFilter(new MyFilter1());
        assertEquals("###hoge###", adaptor.invoke("echo", "echo", new Object[] { "hoge" }));
    }

    public void testFilter2() throws Exception {
        adaptor.addFilter(new MyFilter1());
        adaptor.addFilter(new MyFilter2());
        assertEquals("###Hoge###", adaptor.invoke("echo", "echo", new Object[] { "hoge" }));
    }

    protected void setUp() throws Exception {
        // S2Containerに対するinclude()メソッド
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

    public RMIAdaptorImplTest(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        return new TestSuite(RMIAdaptorImplTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public interface Hello {

        public String say();

    }

    public static class Echo {

        public String echo(String message) {
            return message;
        }

    }

    public static class MyFilter1 implements RMIFilter {

        public Object doFilter(String componentName, String methodName, Object[] args,
                RMIFilterChain chain) throws Throwable {
            return "###" + chain.doFilter(componentName, methodName, args) + "###";
        }

    }

    public static class MyFilter2 implements RMIFilter {

        public Object doFilter(String componentName, String methodName, Object[] args,
                RMIFilterChain chain) throws Throwable {
            return chain.doFilter(componentName, methodName, new Object[] { "Hoge" });
        }

    }

}
