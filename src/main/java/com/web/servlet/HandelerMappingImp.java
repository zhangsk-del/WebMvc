package com.web.servlet;

import com.web.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
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

    @Override
    public Object[] setDI(Method method, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // 分析这个method,获取方法上所有参数对象
        Parameter[] parame = method.getParameters();
        // 存储参数名称
        Object[] parObj = new Object[parame.length];
        for (int i = 0; i < parame.length; i++) {
            // 获取参数注解
            Param parmName = parame[i].getAnnotation(Param.class);
            // 如有有注解，证明为基本类型或String
            if (parmName != null) {
                // 获取请求携带的参数
                String parVaue = request.getParameter(parmName.value());
                if (parVaue != null) {// 参数不为空才注入
                    // 获取参数的类型
                    Class clazzType = parame[i].getType();
                    if (clazzType == String.class) {
                        parObj[i] = new String(parVaue);
                    } else if (clazzType == Integer.class || clazzType == int.class) {
                        parObj[i] = new Integer(parVaue);
                    } else if (clazzType == Long.class || clazzType == long.class) {
                        parObj[i] = new Long(parVaue);
                    } else if (clazzType == Short.class || clazzType == short.class) {
                        parObj[i] = new Short(parVaue);
                    } else if (clazzType == Byte.class || clazzType == byte.class) {
                        parObj[i] = new Byte(parVaue);
                    } else if (clazzType == Float.class || clazzType == float.class) {
                        parObj[i] = new Float(parVaue);
                    } else if (clazzType == Double.class || clazzType == double.class) {
                        parObj[i] = new Double(parVaue);
                    } else if (clazzType == Boolean.class || clazzType == boolean.class) {
                        parObj[i] = new Boolean(parVaue);
                    }
                }
            } else {
                // 对象或者是map
                Class classType = parame[i].getType();
                if (classType.isArray()) {
                    // 为数组
                }
                if (classType == HttpServletRequest.class) {
                    parObj[i] = request;
                    continue;
                }
                if (classType == HttpServletResponse.class) {
                    parObj[i] = response;
                    continue;
                }
                Object object = classType.newInstance();
                if (object instanceof Map) {
                    // 将对象造型成map
                    Map<String, String> map = (Map<String, String>) object;
                    // 获取所有请求的名字
                    Enumeration en = request.getParameterNames();
                    while (en.hasMoreElements()) {
                        String key = (String) en.nextElement();
                        String value = request.getParameter(key);
                        map.put(key, value);
                    }
                    parObj[i] = map;
                } else if (object instanceof Object) {
                    // 是一个对象
                    // 获取所有的属性
                    Field[] files = classType.getDeclaredFields();
                    for (Field field : files) {

                        // 2.set方法赋值法
                        String fieldName = field.getName();
                        // 拼接set方法
                        String one = fieldName.substring(0, 1).toUpperCase();
                        String two = fieldName.substring(1);
                        StringBuilder builderMethod = new StringBuilder("set");
                        builderMethod.append(one);
                        builderMethod.append(two);
                        // 获取属性的类型
                        Class clazzT = field.getType();
                        // 查找set方法
                        Method md = classType.getDeclaredMethod(builderMethod.toString(), clazzT);
                        // 获取请求的参数
                        String value = request.getParameter(fieldName);
                        // 判断该参数的类型 并执行
                        if (clazzT == String.class) {
                            md.invoke(object, value);
                        } else if (clazzT == Integer.class || classType == int.class) {
                            md.invoke(object, Integer.parseInt(value));
                        } else if (clazzT == Float.class || classType == float.class) {
                            md.invoke(object, Float.parseFloat(value));
                        } else if (clazzT == Long.class || classType == Long.class) {
                            md.invoke(object, Long.parseLong(value));
                        } else if (clazzT == Short.class || classType == short.class) {
                            md.invoke(object, Short.parseShort(value));
                        } else if (clazzT == byte.class || classType == byte.class) {
                            md.invoke(object, Byte.parseByte(value));
                        } else if (clazzT == Double.class || classType == double.class) {
                            md.invoke(object, Double.parseDouble(value));
                        } else {
                            //抛异常
                        }
                    }
                    parObj[i] = object;
                }
            }
        }

        // 所有参数处理完毕 返回   Object[]装载的是当前这个method方法所有参数的值
        return parObj;
    }

}
