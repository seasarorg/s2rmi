/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.remoting.rmi.filter.impl;

import java.util.HashMap;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.remoting.rmi.filter.RMIFilter;
import org.seasar.remoting.rmi.filter.RMIFilterChain;

/**
 * RMIサーバプロセスにおいてS2コンテナの{@link org.seasar.framework.container.ExternalContext 外部コンテキスト}を提供します。
 * 
 * @author koichik
 */
public class RMIExternalContextFilter implements RMIFilter {

    public Object doFilter(final String componentName, final String methodName,
            final Object[] args, final RMIFilterChain chain) throws Throwable {
        final S2Container container = SingletonS2ContainerFactory.getContainer();
        final ExternalContext externalContext = container.getExternalContext();
        if (externalContext == null) {
            throw new EmptyRuntimeException("externalContext");
        }
        externalContext.setRequest(new HashMap());

        try {
            return chain.doFilter(componentName, methodName, args);
        }
        finally {
            externalContext.setRequest(null);
            externalContext.setResponse(null);
        }
    }

}
