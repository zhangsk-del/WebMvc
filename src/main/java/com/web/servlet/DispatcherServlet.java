package com.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 根据web.xml配置请求统一处理类
 */
public class DispatcherServlet extends HttpServlet {

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
