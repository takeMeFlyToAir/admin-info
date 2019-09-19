package com.ssd.admin.common;

/**
 * Created by zhaozhirong on 2019/9/2.
 */
public class JsonResp {


    private boolean result;
    private String message;
    private Object data;

    public JsonResp() {
        result = true;
    }
    public JsonResp(String message) {
       this(true,message);
    }

    public JsonResp(boolean result, String message) {
        this.result = result;
        this.message = message;
    }


    public JsonResp isFail(){
        this.result = false;
        return this;
    }


    public JsonResp isSuccess(){
        this.result = true;
        return this;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
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
