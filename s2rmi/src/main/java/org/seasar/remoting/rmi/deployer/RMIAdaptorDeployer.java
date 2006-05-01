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
 * RMIAdaptorをRMIレジストリに登録するクラス.
 * 
 * @author Kenichiro Murata
 */
public class RMIAdaptorDeployer implements Deployer {

    private final static Logger logger = Logger.getLogger(RMIAdaptorDeployer.class);

    private RMIAdaptor adaptor;

    private RMIClientSocketFactory clientSocketFactory = null;

    private RMIServerSocketFactory serverSocketFactory = null;

    private int registryPort = 1099;

    private int servicePort = 0;

    /**
     * @see org.seasar.remoting.common.deployer.Deployer#deploy()
     */
    public void deploy() {
        Registry registry;
        try {
            this.isSetAdaptor();

            if (logger.isDebugEnabled()) {
                logger.log("DRMI0001", new Object[] { Integer.toString(this.registryPort) });
            }

            registry = LocateRegistry.createRegistry(this.registryPort);

            if (this.clientSocketFactory != null && this.serverSocketFactory != null) {
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0002", new Object[] { Integer.toString(this.servicePort) });
                }
                UnicastRemoteObject.exportObject(this.adaptor, this.servicePort,
                        this.clientSocketFactory, this.serverSocketFactory);
            }
            else {
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0003", new Object[] { Integer.toString(this.servicePort) });
                }
                UnicastRemoteObject.exportObject(this.adaptor, this.servicePort);
            }

            if (logger.isDebugEnabled()) {
                logger.log("DRMI0004", new Object[] { RMIAdaptor.EXPORT_NAME });
            }

            registry.rebind(RMIAdaptor.EXPORT_NAME, this.adaptor);
        }
        catch (Exception e) {
            logger.log(e);
        }
    }

    final void isSetAdaptor() throws IllegalArgumentException {
        if (this.adaptor == null) {
            throw new IllegalArgumentException("RMIAdaptor is not defined.");
        }
    }

    /**
     * @param adaptor
     *            adaptor を設定。
     */
    public void setAdaptor(RMIAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    /**
     * @param clientSocketFactory
     *            clientSocketFactory を設定。
     * @param serverSocketFactory
     *            serverSocketFactory を設定。
     */
    public void addCustomSocketFactory(RMIClientSocketFactory clientSocketFactory,
            RMIServerSocketFactory serverSocketFactory) {
        this.clientSocketFactory = clientSocketFactory;
        this.serverSocketFactory = serverSocketFactory;
    }

    /**
     * @param registryPort
     *            registryPort を設定。
     */
    public void setRegistryPort(int registryPort) {
        this.registryPort = registryPort;
    }

    /**
     * @param servicePort
     *            servicePort を設定。
     */
    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }
}
