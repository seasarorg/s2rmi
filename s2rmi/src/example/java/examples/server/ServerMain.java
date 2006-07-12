package examples.server;

import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author Kenichiro Murata
 */
public class ServerMain {

    public static void main(String[] args) {
        S2ContainerFactory.create("examples/server/server.dicon").init();
    }

}