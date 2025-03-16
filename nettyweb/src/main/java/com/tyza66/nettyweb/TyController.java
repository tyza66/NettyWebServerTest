package com.tyza66.nettyweb;

@Controller
public class TyController {
    @Handler(path = "/hello")
    public String helloHandler() {
        return "Hello, tyza66!";
    }
}
