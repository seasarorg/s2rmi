package org.seasar.remoting.rmi.deployer;

import java.rmi.registry.LocateRegistry;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;

/**
 * @author murata
 */
public class RMIAdaptorDeployerTest extends S2TestCase {

    private static String PATH = "test.dicon";

    // 変数の自動セット
    private RMIAdaptorDeployer deployer;

    public void testDeploy() throws Exception {
        deployer.setAdaptor(null);
        deployer.deploy();
    }

    public void testDeploy2() throws Exception {
        deployer.deploy();
        String[] remoteNmae = LocateRegistry.getRegistry(1108).list();

        assertEquals(remoteNmae[0], RMIAdaptor.EXPORT_NAME);
    }

    protected void setUp() throws Exception {
        // S2Containerに対するinclude()メソッド
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

    public RMIAdaptorDeployerTest(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        return new TestSuite(RMIAdaptorDeployerTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }
}
