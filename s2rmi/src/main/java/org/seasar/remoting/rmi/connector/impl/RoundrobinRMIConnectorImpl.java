package org.seasar.remoting.rmi.connector.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.seasar.framework.log.Logger;
import org.seasar.remoting.common.url.UnopenableURLStreamHandler;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;

/**
 * 複数のRMIサーバに対してラウンドロビンで呼び出しを行うコネクタの実装クラスです。
 * 
 * @author koichik
 */
public class RoundrobinRMIConnectorImpl extends RMIConnectorImpl {

    // constants
    /** トータルのリモート呼び出し回数 */
    protected int count;

    // static fields
    private static final Logger logger = Logger.getLogger(RoundrobinRMIConnectorImpl.class);

    // instance fields
    /** ベースURLのリスト */
    protected Map baseURLs = new LinkedHashMap();

    /** {@link RMIAdaptor RMIアダプタ}のリスト */
    protected List adaptorStubs = new ArrayList();

    /**
     * インスタンスを構築します。
     */
    public RoundrobinRMIConnectorImpl() {
    }

    public synchronized void setBaseURLAsString(String baseURL) throws MalformedURLException {
        baseURLs.clear();
        addBaseURLAsString(baseURL);
    }

    /**
     * ベースURLを追加します。
     * 
     * @param baseURL
     *            ベースURLの文字列です
     * @throws MalformedURLException
     *             URLが不正な場合にスローされます
     */
    public synchronized void addBaseURLAsString(final String baseURL) throws MalformedURLException {
        baseURLs.put(baseURL, new URL(null, baseURL, new UnopenableURLStreamHandler(DEFAULT_PORT)));
        adaptorStubs.clear();
    }

    /**
     * ベースURLを削除します。
     * 
     * @param baseURL
     *            ベースURLの文字列です
     */
    public synchronized void removeBaseURLAsString(final String baseURL) {
        baseURLs.remove(baseURL);
        adaptorStubs.clear();
    }

    public void lookup() throws RemoteException, MalformedURLException, NotBoundException {
        for (final Iterator it = baseURLs.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final String urlAsString = (String) entry.getKey();
            final URL url = (URL) entry.getValue();
            final URL targetURL = new URL(url, RMIAdaptor.EXPORT_NAME);
            try {
                adaptorStubs.add(Naming.lookup(targetURL.toString()));
            }
            catch (ConnectException e) {
                logger.log("WRMI0001", new Object[] { urlAsString }, e);
            }
        }
        if (adaptorStubs.isEmpty()) {
            logger.log("ERMI0003", new Object[0]);
            throw new ConnectException("RMI adapter is not available.");
        }
    }

    protected synchronized RMIAdaptor getAdaptor() throws RemoteException, MalformedURLException,
            NotBoundException {
        if (adaptorStubs.isEmpty()) {
            lookup();
        }
        return (RMIAdaptor) adaptorStubs.get(count++ % adaptorStubs.size());
    }

    protected synchronized void resetAdaptor(final RMIAdaptor adaptor) {
        adaptorStubs.remove(adaptor);
    }

}
