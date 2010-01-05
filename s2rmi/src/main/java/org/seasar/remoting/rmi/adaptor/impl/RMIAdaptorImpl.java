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
package org.seasar.remoting.rmi.adaptor.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;
import org.seasar.remoting.rmi.filter.RMIFilter;
import org.seasar.remoting.rmi.filter.RMIFilterChain;

/**
 * RMIを使用してリモートメソッドの呼び出しを行うアダプタの実装クラス.
 * 
 * @author Kenichiro Murata
 */
public class RMIAdaptorImpl implements RMIAdaptor {

    // constants
    public static final String invoker_BINDING = "bindingType=may";

    // instance fields
    protected String invokerName;

    protected ComponentInvoker invoker;

    protected List filters = new ArrayList();

    public Object invoke(final String componetName, final String methodName, final Object[] args)
            throws RemoteException, Exception {
        try {
            return new RMIFilterChainImpl().doFilter(componetName, methodName, args);
        }
        catch (final Exception e) {
            throw e;
        }
        catch (final Error e) {
            throw e;
        }
        catch (final Throwable t) {
            throw new Exception(t);
        }
    }

    /**
     * {@link org.seasar.extension.component.ComponentInvoker}のコンポーネント名を設定します。
     * <p>
     * この名前は{@link #setInvoker(ComponentInvoker) invoker}プロパティが設定されていない場合に、
     * {@link org.seasar.framework.container.factory.SingletonS2ContainerFactory}から取得する際に使用されます。
     * </p>
     * 
     * @param invokerName
     *            {@link org.seasar.extension.component.ComponentInvoker}のコンポーネント名
     */
    public void setInvokerName(final String invokerName) {
        this.invokerName = invokerName;
    }

    /**
     * {@link org.seasar.extension.component.ComponentInvoker}を設定します。
     * 
     * @param invoker
     *            {@link org.seasar.extension.component.ComponentInvoker}
     */
    public void setInvoker(final ComponentInvoker invoker) {
        this.invoker = invoker;
    }

    /**
     * {@link org.seasar.extension.component.ComponentInvoker}を返します。
     * <p>
     * {@link org.seasar.extension.component.ComponentInvoker}が設定されている場合はそれを返します。
     * そうでなければ{@link org.seasar.framework.container.factory.SingletonS2ContainerFactory}から取得して返します。
     * 取得する際には、 もし{@link #setInvokerName(String) invokerName}プロパティが設定されていればそれを、
     * そうでなければ{@link org.seasar.extension.component.ComponentInvoker}クラスをキーとして使用します。
     * </p>
     * 
     * @return {@link org.seasar.extension.component.ComponentInvoker}
     */
    public ComponentInvoker getInvoker() {
        if (invoker != null) {
            return invoker;
        }
        final S2Container container = SingletonS2ContainerFactory.getContainer();
        if (invokerName != null) {
            return (ComponentInvoker) container.getComponent(invokerName);
        }
        return (ComponentInvoker) container.getComponent(ComponentInvoker.class);
    }

    /**
     * RMIフィルタを返します。
     * 
     * @return RMIフィルタ
     */
    public synchronized RMIFilter[] getFilters() {
        return (RMIFilter[]) filters.toArray(new RMIFilter[filters.size()]);
    }

    /**
     * 複数のRMIフィルタを追加します。
     * 
     * @param filters
     *            RMIフィルタ
     */
    public synchronized void setFilters(final RMIFilter[] filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    /**
     * RMIフィルタを追加します。
     * 
     * @param filter
     *            RMIフィルタ
     */
    public synchronized void addFilter(final RMIFilter filter) {
        filters.add(filter);
    }

    /**
     * RMIフィルタチェーンをサポートするためのRMIフィルタ実装クラスです。
     * 
     * @author koichik
     * 
     */
    public class RMIFilterChainImpl implements RMIFilterChain {

        protected RMIFilter[] filters;
        protected int index;

        /**
         * インスタンスを構築します。
         * 
         */
        public RMIFilterChainImpl() {
            filters = getFilters();
        }

        public Object doFilter(final String componentName, final String methodName,
                final Object[] args) throws Throwable {
            if (index < filters.length) {
                return filters[index++].doFilter(componentName, methodName, args, this);
            }
            return getInvoker().invoke(componentName, methodName, args);
        }
    }
}
