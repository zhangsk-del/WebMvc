package com.web.servlet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

interface HandlerMapping {
    /**
     * 获取对象,并获取该类下的所有方法存入diMap
     *
     * @param classForNamePath
     * @return 一个可处理对象的对象
     * @throws ClassNotFoundException 异常抛出
     * @throws IllegalAccessException 异常抛出
     * @throws InstantiationException 异常抛出
     */
    Object getObject(String classForNamePath) throws ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 获取一个controller方法
     *
     * @param methodKey key
     * @param obj       obj
     * @return method
     * @throws NoSuchMethodException 异常抛出
     */
    Method getMethod(String methodKey, Object obj) throws NoSuchMethodException;

    /**
     * 自动注入方法
     *
     * @param method   method
     * @param request  request
     * @param response response
     * @return 返回多个参数解析
     * @throws IllegalAccessException    异常抛出
     * @throws InstantiationException    异常抛出
     * @throws NoSuchMethodException     异常抛出
     * @throws InvocationTargetException 异常抛出
     */
    Object[] setDI(Method method, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;

    /**
     * 扫描注解RequestMapping
     */
    void scanAnnotation();

    /**
     * 响应结果
     *
     * @param method   method
     * @param type     响应类型
     *                 1、welco.jsp/redirect 重定向响应格式;
     *                 2、welco.jsp/forward 转发响应格式;
     *                 3、不带/为json格式响应
     * @param response Response
     * @param request  request
     * @throws Exception Exception
     */
    void response(Method method, Object type, HttpServletResponse response, HttpServletRequest request) throws Exception;

    /**
     * getMethodWithRealNameMap 获取请求与类名的映射关系
     *
     * @param key key
     * @return string
     */
    String getMethodWithRealNameMap(String key);
}


