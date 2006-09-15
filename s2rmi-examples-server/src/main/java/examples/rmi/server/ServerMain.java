package examples.rmi.server;

import org.seasar.framework.container.external.GenericS2ContainerInitializer;

/**
 * @author Kenichiro Murata
 * @author koichik
 */
public class ServerMain {

    public static void main(String[] args) {
        GenericS2ContainerInitializer initializer = new GenericS2ContainerInitializer();
        initializer.initialize();
    }

}