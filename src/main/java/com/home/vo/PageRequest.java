package com.home.vo;

/**
 * Created by Administrator on 2017/9/3.
 */
public class PageRequest {

    private Integer pageSize = 10;

    private Integer pageNumber = 0;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
