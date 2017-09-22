package com.home.vo;

import com.home.model.BaseHourse;
import reactor.core.publisher.Mono;

import java.util.List;

public class FrontData {

    private Integer totalCount;

    private Integer pageNumber;

    private Integer pageSize;

    private Object hourses;

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

    public Object getHourses() {
        return hourses;
    }

    public void setHourses(List<BaseHourse> hourses) {
        this.hourses = hourses;
    }

    public FrontData(){

    }

    public FrontData(Integer totalCount, Integer pageNumber, Integer pageSize, List<BaseHourse> hourses) {
        this.totalCount = totalCount;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.hourses = hourses;
    }

    public static Mono<FrontData> build(Mono<Long> count, Mono<?> hourse, Integer pageNumber, Integer pageSize){
        return count.zipWith(hourse,(sum,house) -> {
            FrontData response = new FrontData();
            response.totalCount = sum.intValue();
            response.hourses = house;
            response.pageNumber = pageNumber;
            response.pageSize = pageSize;
            return response;
        });
    }
}
