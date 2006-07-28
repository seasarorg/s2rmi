package examples.server;

import org.seasar.framework.container.external.GenericS2ContainerInitializer;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author Kenichiro Murata
 * @author koichik
 */
public class ServerMain {

    public static void main(String[] args) {
        S2ContainerFactory.configure("examples/server/s2container.dicon");
        GenericS2ContainerInitializer initializer = new GenericS2ContainerInitializer();
        initializer.setConfigPath("examples/server/server.dicon");
        initializer.initialize();
    }

}