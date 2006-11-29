package org.seasar.remoting.rmi.connector;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.ConnectException;
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

    // constants
    public static final int DEFAULT_PORT = 1099;

    // instance fields
    protected RMIAdaptor adaptorStub;

    /**
     * インスタンスを構築します。
     */
    public RMIConnector() {
        URLStreamHandlerRegistry.registerHandler("rmi", new UnopenableURLStreamHandler(1099));
    }

    public Object invoke(final String componentName, final Method method, final Object[] args)
            throws RemoteException, Exception {
        final RMIAdaptor adaptor = getAdaptor();
        try {
            return adaptor.invoke(componentName, method.getName(), args);
        }
        catch (final ConnectException e) {
            resetAdaptor(adaptor);
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
     */
    public void setBaseURLAsString(final String baseURL) throws MalformedURLException {
        setBaseURL(new URL(null, baseURL, new UnopenableURLStreamHandler(DEFAULT_PORT)));
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
