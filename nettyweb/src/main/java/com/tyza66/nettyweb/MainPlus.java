package com.tyza66.nettyweb;

import java.lang.reflect.Method;
import java.util.Map;

public class MainPlus {
    public static void main(String[] args) throws Exception {
        // 扫描注解并构建路径-方法映射
        Map<String, Method> handlerMap = AnnotationScanner.scanHandlers("com.tyza66.nettyweb");

        // 启动 Netty 服务器
        new NettyWithController(8080, handlerMap).start();
    }
}
