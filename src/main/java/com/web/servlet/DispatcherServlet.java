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

    private Handler handler = new HandlerImp();

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        handler.service(request,response);

    }

}
