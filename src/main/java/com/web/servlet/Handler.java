package com.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Handler {


    /**
     * 核心处理逻辑
     *
     * @param request  请求对象
     * @param response 响应对象
     * @throws ServletException 异常抛出
     * @throws IOException      异常抛出
     */
    void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;


}
