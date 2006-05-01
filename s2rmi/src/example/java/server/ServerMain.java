package server;

import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author Kenichiro Murata
 */
public class ServerMain {
    public static void main(String[] args) {
        S2ContainerFactory.create("server/server.dicon").init();
    }
}