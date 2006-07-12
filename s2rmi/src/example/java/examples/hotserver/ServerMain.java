package examples.hotserver;

import org.seasar.framework.container.external.GenericS2ContainerInitializer;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author koichik
 */
public class ServerMain {

    public static void main(String[] args) {
        S2ContainerFactory.configure("examples/hotserver/s2container.dicon");
        GenericS2ContainerInitializer initializer = new GenericS2ContainerInitializer();
        initializer.setConfigPath("examples/hotserver/server.dicon");
        initializer.initialize();
    }

}