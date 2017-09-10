package com.home.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.model.Hourse;
import com.home.model.Image;
import com.home.repository.HourseRepository;
import com.home.vo.ApiResponse;
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
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Flux<Hourse> hourses = hourseRepository.findByUserIdOrState(sort,userId,0);
        List<Hourse> fbi= hourses.collectList().block();
        fbi = page.getPageSize()*(page.getPageNumber()+1) > fbi.size() ? fbi.subList((page.getPageNumber())*page.getPageSize(),fbi.size()) :
                fbi.subList((page.getPageNumber())*page.getPageSize(),page.getPageSize()*(page.getPageNumber()+1));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",fbi)));
    }

    public Mono<ServerResponse> getHourse(ServerRequest request){
        String hourseId = request.pathVariable("hourseId");
        Mono<Hourse> hourse = hourseRepository.findById(hourseId);
        Mono<ServerResponse> notFound =  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(201,"fail",null)));
        return hourse.flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",data))))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest request){
        Hourse hourse = request.bodyToMono(Hourse.class).block();
        hourse.setUpdateDate(new Date());
        Mono<Hourse> newHourse = hourseRepository.save(hourse);
//        System.out.println(newHourse.block().toString());
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",newHourse.block())));
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        String hourseId = request.pathVariable("hourseId");
        Mono<Void> rs = hourseRepository.deleteById(hourseId);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(new ApiResponse(200,"success",rs.block())));
    }

    public Mono<ServerResponse> create(ServerRequest request){
        Hourse hourse = request.bodyToMono(Hourse.class).block();
        hourse.setCreateDate(new Date());
        Mono<Hourse> newHourse = hourseRepository.save(hourse);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",newHourse.block())));
    }
}
