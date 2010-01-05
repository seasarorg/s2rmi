/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
