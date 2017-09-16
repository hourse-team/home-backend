package com.home.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.model.*;
import com.home.repository.HourseRepository;
import com.home.vo.ApiResponse;
import com.home.vo.NoPagingResponse;
import com.home.vo.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerRequestExtensionsKt;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by Administrator on 2017/8/20.
 */
@Component
public class HourseHandler {
    @Autowired
    HourseRepository hourseRepository;

    public Mono<ServerResponse> getHourses(ServerRequest request){
        String userId = request.pathVariable("userId");
        PageRequest page = request.bodyToMono(PageRequest.class).block();
        if(page == null){
            page = new PageRequest();
        }
        String title = page.getName();
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Flux<BaseHourse> hourses;
        if(title == null) {
            hourses = hourseRepository.findByCreateByOrIsPublic(sort, userId, "0");
        } else {
//            hourses = hourseRepository.findByUserIdOrStateAndTitleLike(sort,userId,0,title);
            hourses = hourseRepository.findByCreateByOrIsPublic(sort,userId,"0").filter(res -> res.getTitle().contains(title));
        }
        List<BaseHourse> fbi= hourses.collectList().block();
        Integer totalCount = fbi.size();
        fbi = page.getPageSize()*(page.getPageNumber()+1) > fbi.size() ? fbi.subList((page.getPageNumber())*page.getPageSize(),fbi.size()) :
                fbi.subList((page.getPageNumber())*page.getPageSize(),page.getPageSize()*(page.getPageNumber()+1));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",fbi,totalCount,page.getPageNumber(),page.getPageSize())));
    }

    public Mono<ServerResponse> getHourse(ServerRequest request){
        String hourseId = request.pathVariable("hourseId");
        Mono<BaseHourse> hourse = hourseRepository.findById(hourseId);
        Mono<ServerResponse> notFound =  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(201,"fail",null)));
        return hourse.flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",data))))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest request){
        String type = request.queryParam("type").get();
        Class<?> clazz = type.equals("1") ? DealHourse.class : RentHouse.class;
        BaseHourse hourse = (BaseHourse) request.bodyToMono(clazz).block();
        hourse.setUpdateDate(new Date());
        hourse.setType(type);
        Mono<BaseHourse> newHourse = hourseRepository.save(hourse);
//        System.out.println(newHourse.block().toString());
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",newHourse.block())));
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String hourseId = request.pathVariable("hourseId");
//        Mono<Void> rs = hourseRepository.deleteById(hourseId);
        String userId = request.bodyToMono(User.class).block().getId();
        Mono<BaseHourse> rs = hourseRepository.findById(hourseId);
        BaseHourse hourse = rs.block();
        hourse.setIsDeleted("1");
        hourse.setUpdateBy(userId);
        Mono<BaseHourse> newHourse = hourseRepository.save(hourse);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(new NoPagingResponse(200,"success",newHourse.block())));
    }

    public Mono<ServerResponse> create(ServerRequest request){
        String type = request.queryParam("type").get();
        Class<?> clazz = type.equals("1") ? DealHourse.class : RentHouse.class;
        BaseHourse hourse = (BaseHourse) request.bodyToMono(clazz).block();
        hourse.setCreateDate(new Date());
        hourse.setType(type);
        Mono<BaseHourse> newHourse = hourseRepository.save(hourse);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",newHourse.block())));
    }

    public Mono<ServerResponse> getAllHourses(ServerRequest request){
        Integer type = Integer.valueOf(request.pathVariable("type"));
        Integer pageSize = Integer.valueOf(request.queryParam("pageSize").orElse("10"));
        Integer pageNumber = Integer.valueOf(request.queryParam("pageNumber").orElse("0"));
        //Integer pageNumber = 0;
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Flux<BaseHourse> all = hourseRepository.findByType(sort,type);
        final int[] totalCount = new int[1];
        List<BaseHourse> houres = all.buffer().blockFirst();
        List<BaseHourse> sunHourse = houres.subList(pageNumber*pageSize,(pageNumber+1)*pageSize > houres.size() ? houres.size() : (pageNumber+1)*pageSize);
//        Disposable disposable = all.buffer().subscribe(data -> {
//             int total = data.size();
//             totalCount[0] = total;
//             data = data.subList(pageNumber*pageSize,(pageNumber+1)*pageSize > data.size() ? data.size() : (pageNumber+1)*pageSize);
//             houres.addAll(data);
//        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(new ApiResponse(200,"success",houres,
                houres.size(),pageNumber,pageSize)));
    }
}
