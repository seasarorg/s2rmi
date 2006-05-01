package org.seasar.remoting.rmi.adaptor;

import java.rmi.RemoteException;

import org.seasar.extension.component.ComponentInvoker;

/**
 * RMIを使用してリモートメソッドの呼び出しを行うアダプタの実装クラス.
 * 
 * @author Kenichiro Murata
 */
public class RMIAdaptorImpl implements RMIAdaptor {

    private ComponentInvoker invoker;

    /**
     * @see org.seasar.remoting.rmi.adaptor.RMIAdaptor#invoke(java.lang.String,
     *      java.lang.String, java.lang.Object[])
     */
    public Object invoke(String componetName, String methodName, Object[] args)
            throws RemoteException, Exception {
        try {
            return this.invoker.invoke(componetName, methodName, args);
        }
        catch (Throwable t) {
            throw new Exception(t);
        }
    }

    /**
     * @param invoker
     *            invoker を設定。
     */
    public void setInvoker(ComponentInvoker invoker) {
        this.invoker = invoker;
    }
}
