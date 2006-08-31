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

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.ClassUtil;

/**
 * @author koichik
 * 
 */
public class RMIConnectorOndemandCreator implements ComponentCreator {

    protected static final String INTERCEPTOR_CLASS_NAME = "org.seasar.remoting.common.interceptor.RemotingInterceptor";

    protected static final String CONNECTOR_CLASS_NAME = "org.seasar.remoting.rmi.connector.RMIConnector";

    protected String remotingInterceptorName = "remoting";

    protected String rmiConnectorComponentName = "rmiConnector";

    protected String baseURLAsString = "rmi://localhost:1108/";

    public void setRemotingInterceptorName(final String remotingInterceptorName) {
        this.remotingInterceptorName = remotingInterceptorName;
    }

    public void setRmiConnectorComponentName(final String rmiConnectorComponentName) {
        this.rmiConnectorComponentName = rmiConnectorComponentName;
    }

    public void setBaseURLAsString(final String baseURLAsString) {
        this.baseURLAsString = baseURLAsString;
    }

    public ComponentDef createComponentDef(final Class clazz) {
        return null;
    }

    public ComponentDef createComponentDef(final String componentName) {
        if (componentName.equals(remotingInterceptorName)) {
            return createRemotingInterceptorComponentDef();
        }
        if (componentName.equals(rmiConnectorComponentName)) {
            return createRMIConnectorComponentDef();
        }
        return null;
    }

    protected ComponentDef createRemotingInterceptorComponentDef() {
        final ComponentDef cd = new ComponentDefImpl(ClassUtil.forName(INTERCEPTOR_CLASS_NAME),
                remotingInterceptorName);
        final PropertyDef pd = new PropertyDefImpl("connector");
        pd.setExpression(new OgnlExpression(rmiConnectorComponentName));
        cd.addPropertyDef(pd);
        return cd;
    }

    protected ComponentDef createRMIConnectorComponentDef() {
        final ComponentDef cd = new ComponentDefImpl(ClassUtil.forName(CONNECTOR_CLASS_NAME),
                rmiConnectorComponentName);
        final PropertyDef pd = new PropertyDefImpl("baseURLAsString", baseURLAsString);
        cd.addPropertyDef(pd);
        final InitMethodDef imd = new InitMethodDefImpl("lookup");
        cd.addInitMethodDef(imd);
        return cd;
    }

}
