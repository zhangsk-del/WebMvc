package com.web.servlet.response;

import java.util.HashMap;

public class ModelSession {

    // 存requset
    private static HashMap<String, String> requestMap = new HashMap();

    // 存session
    private static HashMap<String, String> sessionMap = new HashMap();

    // requset
    public void setRequestAttribute(String key, String value) {
        requestMap.put(key, value);
    }

    String getRequesttAttribute(String key) {
        return requestMap.get(key);
    }

    HashMap<String, String> getRequestAll() {
        return this.requestMap;
    }

    // Session
    public void setSessionAttribute(String key, String value) {
        sessionMap.put(key, value);
    }

    String getSessionMap(String key) {
        return sessionMap.get(key);
    }

    HashMap<String, String> getSessionAll() {
        return this.sessionMap;
    }
}
