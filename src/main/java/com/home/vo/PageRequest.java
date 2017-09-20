package com.home.vo;

/**
 * Created by Administrator on 2017/9/3.
 */
public class PageRequest {

    private Integer pageSize = 10;

    private Integer pageNumber = 0;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public PageRequest(){
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                ", name='" + name + '\'' +
                '}';
    }
}
