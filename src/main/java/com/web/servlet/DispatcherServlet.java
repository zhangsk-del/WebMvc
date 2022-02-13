package com.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


/**
 * 根据web.xml配置请求统一处理类
 *
 *
 */
public class DispatcherServlet extends HttpServlet {

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


    /**
     * 核心处理逻辑
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws ServletException 异常抛出
     * @throws IOException      异常抛出
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.获取请求名
        String url = request.getRequestURI();
        // .do请求
        url = this.analyissURL(url);
        // 2.获取类全名
        String forName = url.substring(0, url.indexOf("."));

        // 3.创建反射对象,并获取类的所有方法

        // 4.获取请求携带的参数

        // 5.获取方法

        // 6.处理请求的参数，将结果返回

        // 7.执行方法 ,并传递request对象和response对象

        // 8.处理响应信息

    }

    // 解析请求的url
    private String analyissURL(String url) {
        //  /wedMVC/login 截取请求名
        url = url.substring(url.lastIndexOf("/") + 1);
        return url;
    }

}
