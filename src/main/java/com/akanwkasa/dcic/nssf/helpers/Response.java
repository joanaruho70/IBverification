package com.akanwkasa.dcic.nssf.helpers;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 12:41 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class Response implements Serializable {
    private int code = 0;
    private String message = null;
    private Object data = null;

    public Response(){};

    public Response(int code, String message, Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public Response(int code, String message){
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
