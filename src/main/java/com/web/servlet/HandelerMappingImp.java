package com.web.servlet;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandelerMappingImp implements HandlerMapping {
    // 将对象的控制权反转（IOC)
    private HashMap<String, Object> iocMap = new HashMap<>();

    // 存类和方法对对应关系----目的自动注入 类对象-----类下所有的方法
    // Map<String, Method> 请求与方法的关系
    private Map<Object, Map<String, Method>> diMap = new HashMap<>();

    @Override
    public Object getObject(String classForNamePath) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clazz = Class.forName(classForNamePath);
        //控制该对象为单例的
        Object obj = this.iocMap.get(classForNamePath);
        if (obj == null) {
            obj = clazz.newInstance();
            this.iocMap.put(classForNamePath, obj);
            // 获取所有方法
            Method[] mds = clazz.getDeclaredMethods();
            // methodMap 请求与方法的关系
            Map<String, Method> methodMap = new HashMap<>();
            for (Method md : mds) {
                String name = md.getName();
                methodMap.put(name, md);
            }
            diMap.put(obj, methodMap);
        }
        return obj;
    }

    @Override
    public Method getMethod(String methodKey, Object obj) throws NoSuchMethodException {
        Map<String, Method> methodMap = diMap.get(obj);
        Method method = methodMap.get(methodKey);
        return method;
    }

}
