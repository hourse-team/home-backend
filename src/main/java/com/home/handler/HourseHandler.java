package com.home.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.model.Hourse;
import com.home.model.Image;
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
        Flux<Hourse> hourses;
        if(title == null) {
            hourses = hourseRepository.findByUserIdOrState(sort, userId, 0);
        } else {
//            hourses = hourseRepository.findByUserIdOrStateAndTitleLike(sort,userId,0,title);
            hourses = hourseRepository.findByUserIdOrState(sort,userId,0).filter(res -> res.getTitle().contains(title));
        }
        List<Hourse> fbi= hourses.collectList().block();
        Integer totalCount = (int)Math.ceil(fbi.size()/page.getPageSize());
        fbi = page.getPageSize()*(page.getPageNumber()+1) > fbi.size() ? fbi.subList((page.getPageNumber())*page.getPageSize(),fbi.size()) :
                fbi.subList((page.getPageNumber())*page.getPageSize(),page.getPageSize()*(page.getPageNumber()+1));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",fbi,totalCount,page.getPageNumber(),page.getPageSize())));
    }

    public Mono<ServerResponse> getHourse(ServerRequest request){
        String hourseId = request.pathVariable("hourseId");
        Mono<Hourse> hourse = hourseRepository.findById(hourseId);
        Mono<ServerResponse> notFound =  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(201,"fail",null)));
        return hourse.flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",data))))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Hourse hourse = request.bodyToMono(Hourse.class).block();
        hourse.setUpdateDate(new Date());
        Mono<Hourse> newHourse = hourseRepository.save(hourse);
//        System.out.println(newHourse.block().toString());
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",newHourse.block())));
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String hourseId = request.pathVariable("hourseId");
        Mono<Void> rs = hourseRepository.deleteById(hourseId);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(new NoPagingResponse(200,"success",rs.block())));
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Hourse hourse = request.bodyToMono(Hourse.class).block();
        hourse.setCreateDate(new Date());
        Mono<Hourse> newHourse = hourseRepository.save(hourse);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",newHourse.block())));
    }

    public Mono<ServerResponse> getAllHourses(ServerRequest request){
        Integer type = Integer.valueOf(request.pathVariable("type"));
        Integer pageSize = Integer.valueOf(request.queryParam("pageSize").orElse("10"));
        Integer pageNumber = Integer.valueOf(request.queryParam("pageNumber").orElse("0"));
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Flux<Hourse> all = hourseRepository.findByStatus(sort,type);
        final int[] totalCount = new int[1];
        List<Hourse> houres = Collections.EMPTY_LIST;
        all.buffer().subscribe(data -> {
             int total = data.size() / pageSize;
             totalCount[0] = total;
             data = data.subList(pageNumber*pageSize,(pageNumber+1)*pageSize > data.size() ? data.size() : (pageNumber+1)*pageSize);
             houres.addAll(data);
        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).header("Access-Control-Allow-Origin","*").body(fromObject(new ApiResponse(200,"success",houres,
                totalCount[0],pageNumber,pageSize)));
    }
}
