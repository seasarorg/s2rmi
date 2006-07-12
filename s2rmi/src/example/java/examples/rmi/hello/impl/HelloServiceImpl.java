package examples.rmi.hello.impl;

import examples.rmi.hello.HelloService;

public class HelloServiceImpl implements HelloService {

    public String say() {
        return "Hello";
    }

}