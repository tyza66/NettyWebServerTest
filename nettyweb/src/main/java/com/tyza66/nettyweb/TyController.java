package com.tyza66.nettyweb;

@Controller
public class TyController {
    @Handler(path = "/hello")
    public String helloHandler() {
        return service() + "Hello, tyza66!";
    }

    @Handler(path = "/ping")
    public String pingHandler() {
        return "pong";
    }

    @Handler(path = "/ping/1")
    public String ping1Handler() {
        return "pong1";
    }

    public String service() {
        return "service: ";
    }
}
