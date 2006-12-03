package org.seasar.remoting.rmi.deployer.impl;

import java.rmi.registry.LocateRegistry;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;

/**
 * @author murata
 */
public class RMIAdaptorDeployerImplTest extends S2TestCase {

    private static String PATH = "test.dicon";

    private RMIAdaptorDeployerImpl deployer;

    protected void setUp() throws Exception {
        include(PATH);
    }

    public void testDeploy() throws Exception {
        deployer.deploy();
        String[] remoteNmae = LocateRegistry.getRegistry(1108).list();

        assertEquals(remoteNmae[0], RMIAdaptor.EXPORT_NAME);
    }

}
