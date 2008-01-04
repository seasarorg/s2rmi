/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

    public void testDeploy2() throws Exception {
        deployer.deploy();
        String[] remoteNmae = LocateRegistry.getRegistry(1108).list();

        assertEquals(remoteNmae[0], RMIAdaptor.EXPORT_NAME);
    }

}
