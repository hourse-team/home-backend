package com.home.vo;

public class NoPagingResponse {

    private Integer status;

    private String msg;

    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public NoPagingResponse(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static NoPagingResponse noFound(){
        return new NoPagingResponse(201,"fail",null);
    }

    public static NoPagingResponse error(String msg){
        return new NoPagingResponse(202,"fail",msg);
    }

    public static NoPagingResponse success(Object data){
        return new NoPagingResponse(200,"success",data);
    }
}
