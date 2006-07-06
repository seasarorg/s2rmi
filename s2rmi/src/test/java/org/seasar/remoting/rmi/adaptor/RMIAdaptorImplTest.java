package org.seasar.remoting.rmi.adaptor;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;
import org.seasar.remoting.rmi.filter.RMIFilter;

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
        catch (NullPointerException expected) {
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
                RMIFilter filter) throws Throwable {
            return "###" + filter.doFilter(componentName, methodName, args, filter) + "###";
        }

    }

    public static class MyFilter2 implements RMIFilter {

        public Object doFilter(String componentName, String methodName, Object[] args,
                RMIFilter filter) throws Throwable {
            return filter.doFilter(componentName, methodName, new Object[] { "Hoge" }, filter);
        }

    }

}
