package org.seasar.remoting.rmi.connector;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.seasar.remoting.common.connector.impl.URLBasedConnector;
import org.seasar.remoting.common.url.URLStreamHandlerRegistry;
import org.seasar.remoting.common.url.UnopenableURLStreamHandler;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;

/**
 * RMIを使用してリモートメソッドの呼び出しを行うコネクタの実装クラス.
 * 
 * @author Kenichiro Murata
 */
public class RMIConnector extends URLBasedConnector {
    private static final int DEFAULT_PORT = 1099;

    private RMIAdaptor adaptorStub;

    /**
     * コンストラクタ.
     */
    public RMIConnector() {
        URLStreamHandlerRegistry.registerHandler("rmi", new UnopenableURLStreamHandler(1099));
    }

    /**
     * @see org.seasar.remoting.common.connector.Connector#invoke(java.lang.String,
     *      java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(String componentName, Method method, Object[] args)
            throws RemoteException, Exception {
        synchronized (this) {
            if (this.adaptorStub == null) {
                this.lookup();
            }
        }

        return this.adaptorStub.invoke(componentName, method.getName(), args);
    }

    /**
     * diconファイルで設定されたbaseURLプロパティを使用して、 RMIレジストリからRMIAdaptorのスタブクラスを取得します.
     * 
     * @throws RemoteException
     *             レジストリへの問い合わせ時にスローされるRMIの例外
     * @throws MalformedURLException
     *             baseURLがrmiプロトコルではない場合の例外
     * @throws NotBoundException
     *             RMIレジストリにRMIAdaptorが未登録の場合の例外
     */
    public void lookup() throws RemoteException, MalformedURLException, NotBoundException {
        URL targetURL = new URL(this.baseURL, RMIAdaptor.EXPORT_NAME);
        this.adaptorStub = (RMIAdaptor) Naming.lookup(targetURL.toString());
    }

    /**
     * ベースURLを文字列で設定します。
     * 
     * @param baseURL
     *            ベースURLの文字列です
     */
    public void setBaseURLAsString(final String urlString) throws MalformedURLException {
        setBaseURL(new URL(null, urlString, new UnopenableURLStreamHandler(DEFAULT_PORT)));
    }
}
