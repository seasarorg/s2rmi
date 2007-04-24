/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.remoting.rmi.deployer.impl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.remoting.common.deployer.Deployer;
import org.seasar.remoting.rmi.adaptor.RMIAdaptor;

/**
 * RMIAdaptorをRMIレジストリに登録するクラスです。
 * 
 * @author Kenichiro Murata
 */
public class RMIAdaptorDeployerImpl implements Deployer {

    // constants
    public static final String adaptor_BINDING = "bindingType=must";

    // static fields
    private static final Logger logger = Logger.getLogger(RMIAdaptorDeployerImpl.class);

    // instance fields
    protected RMIAdaptor adaptor;

    protected RMIClientSocketFactory clientSocketFactory;

    protected RMIServerSocketFactory serverSocketFactory;

    protected int registryPort = 1099;

    protected int servicePort = 0;

    protected Registry registry;

    /**
     * RMIAdaptorをリモートオブジェクトとしてデプロイします。
     * 
     */
    public void deploy() {
        try {
            registry = LocateRegistry.createRegistry(registryPort);
            if (logger.isDebugEnabled()) {
                logger.log("DRMI0001", new Object[] { Integer.toString(registryPort) });
            }

            if (clientSocketFactory != null && serverSocketFactory != null) {
                UnicastRemoteObject.exportObject(adaptor, servicePort, clientSocketFactory,
                        serverSocketFactory);
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0002", new Object[] { Integer.toString(servicePort) });
                }
            }
            else {
                UnicastRemoteObject.exportObject(adaptor, servicePort);
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0003", new Object[] { Integer.toString(servicePort) });
                }
            }

            registry.rebind(RMIAdaptor.EXPORT_NAME, adaptor);
            if (logger.isDebugEnabled()) {
                logger.log("DRMI0004", new Object[0]);
            }
        }
        catch (final Exception e) {
            throw new SRuntimeException("ERMI0001", new Object[0], e);
        }
    }

    /**
     * 公開しているRMIAdaptorをアンデプロイします。
     * 
     */
    public void undeploy() {
        try {
            if (registry != null) {
                registry.unbind(RMIAdaptor.EXPORT_NAME);
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0005", new Object[0]);
                }
            }
        }
        catch (final Exception e) {
            logger.log("ERMI0002", new Object[0], e);
        }

        try {
            if (adaptor != null) {
                UnicastRemoteObject.unexportObject(adaptor, true);
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0006", new Object[0]);
                }
            }
        }
        catch (final Exception e) {
            logger.log("ERMI0002", new Object[0], e);
        }

        try {
            if (registry != null) {
                UnicastRemoteObject.unexportObject(registry, true);
                if (logger.isDebugEnabled()) {
                    logger.log("DRMI0007", new Object[0]);
                }
            }
        }
        catch (final Exception e) {
            logger.log("ERMI0002", new Object[0], e);
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
