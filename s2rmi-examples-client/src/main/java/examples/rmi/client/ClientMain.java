package examples.rmi.client;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.hotdeploy.HotdeployBehavior;
import org.seasar.framework.container.impl.S2ContainerBehavior;

import examples.rmi.service.HelloService;

/**
 * @author Kenichiro Murata
 */
public class ClientMain {

    public static void main(String[] args) {
        SingletonS2ContainerFactory.init();
        S2Container container = SingletonS2ContainerFactory.getContainer();
        container.init();

        S2ContainerBehavior.Provider behavior = S2ContainerBehavior.getProvider();
        if (behavior instanceof HotdeployBehavior) {
            ((HotdeployBehavior) behavior).start();
        }

        HelloService hello = (HelloService) container.getComponent(HelloService.class);
        System.out.println(hello.say());

        if (behavior instanceof HotdeployBehavior) {
            ((HotdeployBehavior) behavior).stop();
        }

        SingletonS2ContainerFactory.destroy();
    }
}