package com.tyza66.nettyweb;

@Controller
public class TyController {
    @Handler(path = "/hello")
    public String helloHandler() {
        return service() + "Hello, tyza66!";
    }

    public String service() {
        return "service: ";
    }
}
