package examples.client;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.rmi.hello.HelloService;

/**
 * @author Kenichiro Murata
 */
public class ClientMain {

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create("examples/client/client.dicon");
        container.init();

        HelloService hello = (HelloService) container.getComponent(HelloService.class);
        System.out.println(hello.say());
    }
}