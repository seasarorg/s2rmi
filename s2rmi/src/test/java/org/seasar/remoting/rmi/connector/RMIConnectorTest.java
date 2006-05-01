package org.seasar.remoting.rmi.connector;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author murata
 */
public class RMIConnectorTest extends S2TestCase {

    private static String PATH = "test.dicon";

    // 変数の自動セット
    private RMIConnector connector;

    public void testLookup() throws Exception {
        connector.setBaseURL(new URL("http://localhost:1099/"));
        try {
            connector.lookup();
            fail();
        }
        catch (Exception e) {
            assertEquals(MalformedURLException.class, e.getClass());
        }
    }

    public void testLookup2() throws Exception {
        try {
            connector.lookup();
            fail();
        }
        catch (Exception e) {
            assertEquals(java.rmi.ConnectException.class, e.getClass());
        }
    }

    protected void setUp() throws Exception {
        // S2Containerに対するinclude()メソッド
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

    public RMIConnectorTest(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        return new TestSuite(RMIConnectorTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
