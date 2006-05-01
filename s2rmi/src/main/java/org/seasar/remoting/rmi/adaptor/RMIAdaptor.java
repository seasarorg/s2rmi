package org.seasar.remoting.rmi.adaptor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMIを使用してリモートメソッドの呼び出しを行うアダプタのインタフェース.
 * 
 * @author Kenichiro Murata
 */
public interface RMIAdaptor extends Remote {

    public static String EXPORT_NAME = "RMIAdaptor";

    /**
     * RMIを使用してリモートメソッドの呼び出しを実行し、その結果を返します.
     * 
     * @param componentName
     *            リモートオブジェクト名称
     * @param methodName
     *            呼び出すメソッド名称
     * @param args
     *            リモートオブジェクトのメソッド呼び出しに渡される引数値を格納するオブジェクト配列
     * @return リモートオブジェクトに対するメソッド呼び出しからの戻り値
     * @throws RemoteException
     *             リモートオブジェクトに対するメソッド呼び出し時にスローされるRMIの例外
     * @throws Exception
     *             リモートオブジェクトに対するメソッド呼び出しからスローされる例外
     */
    public Object invoke(String componentName, String methodName, Object[] args)
            throws RemoteException, Exception;
}
