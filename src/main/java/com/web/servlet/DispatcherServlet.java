package com.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 根据web.xml配置请求统一处理类
 */
public class DispatcherServlet extends HttpServlet {

    private InitConfig initConfig = new InitConfig();

    private HandlerMapping handlerMapping = new HandelerMappingImp();

    // 该方法对象创建时加载
    public void init() {
        try {
            initConfig.load();
            handlerMapping.scanAnnotation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 核心处理类，分发请求
     *
     * @param request  request
     * @param response response
     * @throws ServletException 异常抛出
     * @throws IOException      异常抛出
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        try {
            // 1.获取请求名
            String url = request.getRequestURI();
            // .do请求
            url = initConfig.analyissURL(url);
            // 2.获取类全名
            String className = url.substring(0, url.indexOf("."));

            String classNamePath = initConfig.getConfigMap(className);

            if (classNamePath == null) {// 证明类上没有配置
                classNamePath = handlerMapping.getMethodWithRealNameMap(url);
            }

            // 3.创建反射对象,并获取类的所有方法
            Object obj = handlerMapping.getObject(classNamePath);

            // 4.获取请求携带的参数
            String md = request.getParameter("method");
            if (md == null) {// 证明不是类名.do方式请求
                md = url;
            }
            // 5.获取方法
            Method method = handlerMapping.getMethod(md, obj);

            // 6.处理请求的参数，将结果返回
            Object[] objects = handlerMapping.setDI(method, request, response);

            // 7.执行方法 ,并传递request对象和response对象
            Object path = method.invoke(obj, objects);

            // 8.处理响应信息
            handlerMapping.response(method, path, response, request);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
