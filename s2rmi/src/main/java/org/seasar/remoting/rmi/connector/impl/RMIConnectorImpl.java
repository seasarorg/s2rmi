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
package org.seasar.remoting.rmi.connector.impl;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.seasar.remoting.common.connector.impl.URLBasedConnector;
import org.seasar.remoting.common.url.URLStreamHandlerRegistry;
import org.seasar.remoting.common.url.UnopenableURLStreamHandler;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;

/**
 * RMIを使用してリモートメソッドの呼び出しを行うコネクタの実装クラスです。
 * 
 * @author Kenichiro Murata
 */
public class RMIConnectorImpl extends URLBasedConnector {

    // constants
    public static final int DEFAULT_PORT = 1099;

    // instance fields
    protected RMIAdaptor adaptorStub;

    /**
     * インスタンスを構築します。
     */
    public RMIConnectorImpl() {
        URLStreamHandlerRegistry.registerHandler("rmi", new UnopenableURLStreamHandler(1099));
    }

    public Object invoke(final String componentName, final Method method, final Object[] args)
            throws RemoteException, Exception {
        RMIAdaptor adaptor = getAdaptor();
        try {
            try {
                return adaptor.invoke(componentName, method.getName(), args);
            }
            catch (final NoSuchObjectException e) {
                resetAdaptor(adaptor);
                adaptor = getAdaptor();
                return adaptor.invoke(componentName, method.getName(), args);
            }
        }
        catch (final Exception e) {
            resetAdaptorIfNecessary(e, adaptor);
            throw e;
        }
    }

    /**
     * diconファイルで設定されたbaseURLプロパティを使用して、 RMIレジストリからRMIAdaptorのスタブクラスを取得します。
     * 
     * @throws RemoteException
     *             レジストリへの問い合わせ時にスローされるRMIの例外
     * @throws MalformedURLException
     *             baseURLがrmiプロトコルではない場合の例外
     * @throws NotBoundException
     *             RMIレジストリにRMIAdaptorが未登録の場合の例外
     */
    public void lookup() throws RemoteException, MalformedURLException, NotBoundException {
        final URL targetURL = new URL(baseURL, RMIAdaptor.EXPORT_NAME);
        adaptorStub = (RMIAdaptor) Naming.lookup(targetURL.toString());
    }

    /**
     * ベースURLを文字列で設定します。
     * 
     * @param baseURL
     *            ベースURLの文字列です
     * @throws MalformedURLException
     *             URLが不正な場合にスローされます
     */
    public synchronized void setBaseURLAsString(final String baseURL) throws MalformedURLException {
        setBaseURL(new URL(null, baseURL, new UnopenableURLStreamHandler(DEFAULT_PORT)));
        adaptorStub = null;
    }

    /**
     * RMIアダプタを返します。
     * <p>
     * RMIアダプタを取得済みの場合はそれを返します。
     * RMIアダプタが未取得の場合、またはリセットされた場合は新たにRMIアダプタをルックアップして返します。
     * </p>
     * 
     * @return RMIアダプタ
     * @throws RemoteException
     *             レジストリへの問い合わせ時にスローされるRMIの例外
     * @throws MalformedURLException
     *             baseURLがrmiプロトコルではない場合の例外
     * @throws NotBoundException
     *             RMIレジストリにRMIAdaptorが未登録の場合の例外
     */
    protected synchronized RMIAdaptor getAdaptor() throws RemoteException, MalformedURLException,
            NotBoundException {
        if (adaptorStub == null) {
            lookup();
        }
        return adaptorStub;
    }

    /**
     * 例外をチェックして、必要であればRMIアダプタをリセットします。
     * <p>
     * 例外が{@link ConnectException}または{@link NoSuchObjectException}のいずれかであれば、
     * RMIアダプタをリセットします。
     * </p>
     * 
     * @param e
     *            発生した例外
     * @param adaptor
     *            リセットするRMIアダプタ
     */
    protected void resetAdaptorIfNecessary(final Throwable e, final RMIAdaptor adaptor) {
        if (e instanceof ConnectException || e instanceof NoSuchObjectException) {
            resetAdaptor(adaptor);
        }
    }

    /**
     * RMIアダプタをリセットします。
     * <p>
     * コネクション障害が発生した場合などに利用不能となったRMIアダプタを破棄するために呼び出されます。
     * </p>
     * 
     * @param adaptor
     *            利用不能となったRMIアダプタ
     */
    protected synchronized void resetAdaptor(final RMIAdaptor adaptor) {
        if (adaptorStub == adaptor) {
            adaptorStub = null;
        }
    }

}
