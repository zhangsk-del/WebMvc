package com.web.servlet.response;

import com.google.gson.Gson;

public class Message {

    private String result;

    private Object data;

    public String toJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public Message(Object data) {
        this.data = data;
    }

    public Message() {
    }

    public Message(String result) {
        this.result = result;
    }

    public Message(String result, Object data) {
        this.result = result;
        this.data = data;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
