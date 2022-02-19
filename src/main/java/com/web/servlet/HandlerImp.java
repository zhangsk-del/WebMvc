package com.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HandlerImp implements Handler {

    // 将对象的控制权反转（IOC)
    private HashMap<String, Object> iocMap = new HashMap<>();

    // 存类和方法对对应关系----目的自动注入 类对象-----类下所有的方法
    // Map<String, Method> 请求与方法的关系
    private Map<Object, Map<String, Method>> diMap = new HashMap<>();


    // 存储配置文件信息，类名-包全名
    private HashMap<String, String> configMap = new HashMap<>();


    // 该方法对象创建时加载
    public void init() {
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Method getMethod(String method, Object obj) throws NoSuchMethodException {
        Map<String, Method> methodMap = diMap.get(obj);
        Method md = methodMap.get(method);
        return md;
    }

    // 读取配置文件，获取包名
    private void load() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("Application.properties");
        Properties properties = new Properties();
        properties.load(is);
        // 遍历集合
        Enumeration keys = properties.propertyNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = properties.getProperty(key);
            configMap.put(key, value);
        }
    }

    // 获取对象,并获取该类下的所有方法存入diMap
    private Object getObject(String classForNamePath) throws
            ClassNotFoundException, IllegalAccessException, InstantiationException {
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
    // 解析请求的url
    private String analyissURL(String url) {
        //  /wedMVC/login 截取请求名
        url = url.substring(url.lastIndexOf("/") + 1);
        return url;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1.获取请求名
            String url = request.getRequestURI();
            // .do请求
            url = this.analyissURL(url);
            // 2.获取类全名
            String className = url.substring(0, url.indexOf("."));

            String classNamePath = configMap.get(className);

            // 3.创建反射对象,并获取类的所有方法
            Object obj = getObject(classNamePath);

            // 4.获取请求携带的参数
            String md = request.getParameter("method");
            if (md == null) {//证明不是类名.do方式请求
                md = url;
            }
            // 5.获取方法
            Method method = this.getMethod(md, obj);

            // 6.处理请求的参数，将结果返回

            // 7.执行方法 ,并传递request对象和response对象

            // 8.处理响应信息
            getObject(classNamePath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
