package org.seasar.remoting.rmi.connector.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
    protected List baseURLs = new ArrayList();

    /** {@link RMIAdaptor RMIアダプタ}のリスト */
    protected List adaptorStubs = new ArrayList();

    /**
     * インスタンスを構築します。
     */
    public RoundrobinRMIConnectorImpl() {
    }

    /**
     * ベースURLを文字列で追加します。
     * 
     * @param baseURL
     *            ベースURLの文字列です
     * @throws MalformedURLException
     *             URLが不正な場合にスローされます
     */
    public void addBaseURLAsString(String baseURL) throws MalformedURLException {
        baseURLs.add(new URL(null, baseURL, new UnopenableURLStreamHandler(DEFAULT_PORT)));
    }

    public void lookup() throws RemoteException, MalformedURLException, NotBoundException {
        for (int i = 0; i < baseURLs.size(); ++i) {
            final URL targetURL = new URL((URL) baseURLs.get(i), RMIAdaptor.EXPORT_NAME);
            try {
                adaptorStubs.add(Naming.lookup(targetURL.toString()));
            }
            catch (ConnectException e) {
                logger.log("WRMI0001", new Object[0], e);
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
