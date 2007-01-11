/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.remoting.rmi.filter.RMIFilter;
import org.seasar.remoting.rmi.filter.RMIFilterChain;

/**
 * S2RMIサーバ上でHOT deployをサポートするための｛@link org.seasar.remoting.rmi.filter.RMIFilter
 * RMIフィルタ}です。
 * 
 * @author koichik
 */
public class RMIHotdeployFilter implements RMIFilter {

    public Object doFilter(final String componentName, final String methodName,
            final Object[] args, final RMIFilterChain chain) throws Throwable {
        HotdeployUtil.start();
        try {
            return chain.doFilter(componentName, methodName, args);
        }
        finally {
            HotdeployUtil.stop();
        }
    }

}
