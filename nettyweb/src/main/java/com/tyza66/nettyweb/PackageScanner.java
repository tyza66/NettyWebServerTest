package com.tyza66.nettyweb;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PackageScanner {

    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/'); // 将包名转换为文件路径
        String classPath = Thread.currentThread().getContextClassLoader().getResource(path).getPath();

        File directory = new File(classPath);
        if (directory.exists()) {
            // 遍历目录下所有 .class 文件
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().replace(".class", "");
                    classes.add(Class.forName(className)); // 加载类
                }
            }
        }
        return classes;
    }
}
