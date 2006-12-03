package org.seasar.remoting.rmi.connector.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.seasar.extension.unit.S2TestCase;

/**
 * @author murata
 */
public class RMIConnectorImplTest extends S2TestCase {

    private static String PATH = "test.dicon";

    private RMIConnectorImpl connector;

    protected void setUp() throws Exception {
        // S2Containerに対するinclude()メソッド
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

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

}
