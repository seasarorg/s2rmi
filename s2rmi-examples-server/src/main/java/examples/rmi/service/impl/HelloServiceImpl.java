package examples.rmi.service.impl;

import examples.rmi.service.HelloService;

public class HelloServiceImpl implements HelloService {

    public String say() {
        return "Hello";
    }

}