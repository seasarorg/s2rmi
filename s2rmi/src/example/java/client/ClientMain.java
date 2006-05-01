package client;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import service.Hello;


/**
 * @author Kenichiro Murata
 */
public class ClientMain {
    public static void main(String[] args) {
        S2Container container = S2ContainerFactory
                .create("client/client.dicon");
        
        container.init();
        
        Hello hello = (Hello) container
                .getComponent(service.Hello.class);

        System.out.println(hello.say());
    }
}