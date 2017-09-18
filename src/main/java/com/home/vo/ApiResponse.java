package com.home.vo;

import com.home.model.BaseHourse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Administrator on 2017/9/2.
 */
public class ApiResponse {
    private Integer status;

    private String msg;

    private Object data;

    private Integer totalCount;

    private Integer pageNumber;

    private Integer pageSize;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

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

    public ApiResponse(){

    }

    public ApiResponse(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }


    public ApiResponse(Integer status, String msg, Object data, Integer totalCount, Integer pageNumber, Integer pageSize) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.totalCount = totalCount;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static Mono<ApiResponse> build(Mono<Long> count, Mono<List<BaseHourse>> hourse, Mono<PageRequest> page){
        return count.zipWith(hourse,(sum,house) -> {
            ApiResponse response = new ApiResponse();
            response.totalCount = sum.intValue();
            response.data = house;
            return response;
        }).zipWith(page,(response,request) -> {
            response.pageNumber = request.getPageNumber();
            response.pageSize = request.getPageSize();
            return response;
        });
    }
}
