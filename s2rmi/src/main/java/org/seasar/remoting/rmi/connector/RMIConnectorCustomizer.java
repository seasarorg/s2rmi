/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.remoting.rmi.connector;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;

/**
 * @author koichik
 * 
 */
public class RMIConnectorCustomizer implements ComponentCustomizer {

    protected static final String RMI_CONNECTOR_CLASS_NAME = "org.seasar.remoting.rmi.connector.RMIConnector";

    protected String baseURLAsString = "rmi://localhost:1108/";

    public void setBaseURLAsString(final String baseURLAsString) {
        this.baseURLAsString = baseURLAsString;
    }

    public void customize(final ComponentDef componentDef) {
        if (componentDef.getComponentClass().getName().equals(RMI_CONNECTOR_CLASS_NAME)) {
            final PropertyDef propertyDef = new PropertyDefImpl("baseURLAsString", baseURLAsString);
            componentDef.addPropertyDef(propertyDef);
            final InitMethodDef initMethodDef = new InitMethodDefImpl("lookup");
            componentDef.addInitMethodDef(initMethodDef);
        }
    }

}
