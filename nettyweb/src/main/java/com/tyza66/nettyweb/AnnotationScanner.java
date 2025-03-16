package com.tyza66.nettyweb;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationScanner {

//    public static List<Class<?>> scanPackage(String packageName, Class<? extends Annotation> annotationClass) {
//        List<Class<?>> annotatedClasses = new ArrayList<>();
//        String path = packageName.replace('.', '/'); // 转换包路径为文件路径
//        String classPath = Thread.currentThread().getContextClassLoader().getResource(path).getPath();
//
//        File directory = new File(classPath);
//        if (directory.exists()) {
//            for (File file : directory.listFiles()) {
//                if (file.getName().endsWith(".class")) {
//                    try {
//                        String className = packageName + "." + file.getName().replace(".class", "");
//                        Class<?> clazz = Class.forName(className);
//
//                        // 判断类是否被指定注解标记
//                        if (clazz.isAnnotationPresent(annotationClass)) {
//                            annotatedClasses.add(clazz);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return annotatedClasses;
//    }

    // 扫描包路径中的所有类
    public static Set<Class<?>> scanPackage(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        String classPath = Thread.currentThread().getContextClassLoader().getResource(path).getPath();

        File directory = new File(classPath);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    classes.addAll(scanPackage(packageName + "." + file.getName())); // 递归子包
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }

    // 扫描带有 @Controller 和 @Handler 注解的类和方法
    public static Map<String, Method> scanHandlers(String packageName) throws Exception {
        Map<String, Method> handlerMap = new HashMap<>();
        Set<Class<?>> classes = scanPackage(packageName);

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Handler.class)) {
                        Handler handler = method.getAnnotation(Handler.class); // 获得方法上的 @Handler 注解
                        handlerMap.put(handler.path(), method); // 将路径和方法自身存入映射
                    }
                }
            }
        }
        return handlerMap;
    }
}