package org.seasar.remoting.rmi.deployer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import org.seasar.framework.log.Logger;
import org.seasar.remoting.common.deployer.Deployer;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;

/**
 * RMIAdaptorをRMIレジストリに登録するクラスです。
 * 
 * @author Kenichiro Murata
 */
public class RMIAdaptorDeployer implements Deployer {

    // static fields
    private final static Logger logger = Logger.getLogger(RMIAdaptorDeployer.class);

    // instance fields
    private RMIAdaptor adaptor;

    private RMIClientSocketFactory clientSocketFactory = null;

    private RMIServerSocketFactory serverSocketFactory = null;

    private int registryPort = 1099;

    private int servicePort = 0;

    public void deploy() {
        Registry registry;
        try {
            isSetAdaptor();

            if (logger.isDebugEnabled()) {
                logger.log("DRMI0001", new Object[] { Integer.toString(registryPort) });
            }

            registry = LocateRegistry.createRegistry(registryPort);

            if (clientSocketFactory != null && serverSocketFactory != null) {
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0002", new Object[] { Integer.toString(servicePort) });
                }
                UnicastRemoteObject.exportObject(adaptor, servicePort, clientSocketFactory,
                        serverSocketFactory);
            }
            else {
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0003", new Object[] { Integer.toString(servicePort) });
                }
                UnicastRemoteObject.exportObject(adaptor, servicePort);
            }

            if (logger.isDebugEnabled()) {
                logger.log("DRMI0004", new Object[] { RMIAdaptor.EXPORT_NAME });
            }

            registry.rebind(RMIAdaptor.EXPORT_NAME, adaptor);
        }
        catch (final Exception e) {
            logger.log(e);
        }
    }

    final void isSetAdaptor() throws IllegalArgumentException {
        if (adaptor == null) {
            throw new IllegalArgumentException("RMIAdaptor is not defined.");
        }
    }

    /**
     * RMIアダプタを設定します。
     * 
     * @param adaptor
     *            RMIアダプタ
     */
    public void setAdaptor(final RMIAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    /**
     * ソケットファクトリを追加します。
     * 
     * @param clientSocketFactory
     *            クライアントソケットファクトリ
     * @param serverSocketFactory
     *            サーバソケットファクトリ
     */
    public void addCustomSocketFactory(final RMIClientSocketFactory clientSocketFactory,
            final RMIServerSocketFactory serverSocketFactory) {
        this.clientSocketFactory = clientSocketFactory;
        this.serverSocketFactory = serverSocketFactory;
    }

    /**
     * RMIレジストリのポート番号を設定します。
     * 
     * @param registryPort
     *            RMIレジストリのポート番号
     */
    public void setRegistryPort(final int registryPort) {
        this.registryPort = registryPort;
    }

    /**
     * RMIサーバのポートを設定します。
     * 
     * @param servicePort
     *            RMIサーバのポート
     */
    public void setServicePort(final int servicePort) {
        this.servicePort = servicePort;
    }

}
