package com.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class InitConfig {

    // 存储配置文件信息，类名-包全名
    private HashMap<String, String> configMap = new HashMap<>();

    // 读取配置文件，获取包名
    public void load() throws IOException {
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

    // 解析请求的url
    public String analyissURL(String url) {
        //  /wedMVC/login 截取请求名
        url = url.substring(url.lastIndexOf("/") + 1);
        return url;
    }

    public String getConfigMap(String key) {
        return configMap.get(key);
    }
}
