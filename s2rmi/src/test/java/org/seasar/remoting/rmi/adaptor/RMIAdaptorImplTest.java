package org.seasar.remoting.rmi.adaptor;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.MethodNotFoundRuntimeException;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;

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
        catch (Exception e) {
            assertEquals(org.seasar.framework.container.ComponentNotFoundRuntimeException.class, e
                    .getCause().getClass());
        }
    }

    public void testInvoke2() throws Exception {
        try {
            adaptor.invoke("hello2", "say", null);
            fail();
        }
        catch (Exception e) {
            assertEquals(TooManyRegistrationRuntimeException.class, e.getCause().getClass());
        }
    }

    public void testInvoke3() throws Exception {
        try {
            adaptor.invoke("hello", "hello", null);
            fail();
        }
        catch (Exception e) {
            assertEquals(MethodNotFoundRuntimeException.class, e.getCause().getClass());
        }
    }

    public void testInvoke4() throws Exception {
        try {
            String[] args = { "hello" };
            adaptor.invoke("hello", "say", args);
            fail();
        }
        catch (Exception e) {
            assertEquals(MethodNotFoundRuntimeException.class, e.getCause().getClass());
        }
    }

    public void testInvoke5() throws Exception {
        adaptor.setInvoker(null);
        try {
            adaptor.invoke("hello", "say", null);
            fail();
        }
        catch (Exception e) {
            assertEquals(NullPointerException.class, e.getCause().getClass());
        }
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

    interface Hello {

        public String say();
    }
}
