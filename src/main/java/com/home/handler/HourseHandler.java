package com.home.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.model.*;
import com.home.repository.HourseRepository;
import com.home.util.ServerResponseUtil;
import com.home.vo.ApiResponse;
import com.home.vo.NoPagingResponse;
import com.home.vo.PageRequest;
import org.slf4j.LoggerFactory;
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
import java.util.logging.Logger;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by Administrator on 2017/8/20.
 */
@Component
public class HourseHandler {
    @Autowired
    HourseRepository hourseRepository;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HourseHandler.class);

    private static final ApiResponse noData = new ApiResponse(202,"page parameter error",Collections.EMPTY_LIST);

    private static final NoPagingResponse error = NoPagingResponse.error("参数不对，服务器拒绝响应");

    public Mono<ServerResponse> getHourses(ServerRequest request){
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Mono<PageRequest> page = request.bodyToMono(PageRequest.class).switchIfEmpty(Mono.just(new PageRequest()));
        Flux<BaseHourse> hourses = hourseRepository.findByCreateByOrIsPublic(sort,request.pathVariable("userId"),"0")
                .filter(res -> {
                    String title = page.block().getName();
                    boolean bool;
                    if(title == null || title.equals("")){
                        bool = 1 == 1;
                    } else {
                        bool = res.getTitle().contains(title);
                    }
                    return bool;
                });
        Mono<ApiResponse> build = ApiResponse.build(hourses.count(), hourses.collectList().zipWith(page, (list, pag) -> {
            Integer start = pag.getPageNumber()*pag.getPageSize();
            Integer end = (pag.getPageNumber()+1)*pag.getPageSize();
            list = end > list.size() ? list.subList(start,list.size()) : list.subList(start,end);
            return list;
        }),page).onErrorReturn(noData);
        return ServerResponseUtil.createByMono(build,ApiResponse.class);
    }

    public Mono<ServerResponse> getHourse(ServerRequest request){
        return hourseRepository.findById(request.pathVariable("hourseId"))
                .flatMap(data -> ServerResponseUtil.createResponse(NoPagingResponse.success(data)))
                .switchIfEmpty(ServerResponseUtil.createResponse(NoPagingResponse.noFound()));
    }

    public Mono<ServerResponse> findHourse(ServerRequest request){
        return hourseRepository.findById(request.pathVariable("hourseId"))
                .flatMap(data ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(fromObject(new NoPagingResponse(200,"success",data)))
                );
    }

    public Mono<ServerResponse> update(ServerRequest request){
        String type = request.queryParam("type").get();
        Class<?> clazz = type.equals("1") ? DealHourse.class : RentHouse.class;
        return hourseRepository.save(request.bodyToMono(clazz))
                .flatMap(data -> ServerResponseUtil.createResponse(NoPagingResponse.success(data)))
                .onErrorReturn(ServerResponseUtil.createResponse(NoPagingResponse.error("参数不对，服务器拒绝响应")).block());
    }

    public Mono<ServerResponse> delete(ServerRequest request){
        Mono<BaseHourse> hourse = hourseRepository.save(hourseRepository.findById(request.pathVariable("hourseId")).map(res -> {
            res.setIsDeleted("1");
            res.setUpdateBy(request.bodyToMono(User.class).map(User::getId));
            return res;
        }));
        return hourse.flatMap(data -> ServerResponseUtil.createByMono(hourse,BaseHourse.class))
                .onErrorReturn(ServerResponseUtil.createResponse(NoPagingResponse.error("参数不对，服务器拒绝响应")).block());
    }

    public Mono<ServerResponse> create(ServerRequest request){
        String type = request.queryParam("type").get();
        Class<?> clazz = type.equals("1") ? DealHourse.class : RentHouse.class;
        Mono<BaseHourse> hourseMono = (Mono<BaseHourse>)request.bodyToMono(clazz);
        return ServerResponseUtil.createByMono(
                hourseRepository.save(hourseMono.map(hourse -> {
                    hourse.setCreateDate(new Date());
                    hourse.setType(type);
                    return hourse;
                })).flatMap(r -> {
                    NoPagingResponse response =  NoPagingResponse.success(r);
                    return Mono.just(response);
                }).onErrorReturn(error),NoPagingResponse.class);
    }

    public Mono<ServerResponse> getAllHourses(ServerRequest request){
        String type = request.pathVariable("type");
        Integer pageSize = Integer.valueOf(request.queryParam("pageSize").orElse("10"));
        Integer pageNumber = Integer.valueOf(request.queryParam("pageNumber").orElse("0"));
        //Integer pageNumber = 0;
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Flux<BaseHourse> all = hourseRepository.findByType(sort,type);
        final int[] totalCount = new int[1];
        List<BaseHourse> houres = all.buffer().blockFirst();
//        logger.info(houres.toString());
        Mono<ServerResponse> response = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(new ApiResponse(200,"success",Collections.EMPTY_LIST,
                0,pageNumber,pageSize)));
        if(houres == null){
            return response;
        }
        if(pageNumber*pageSize > houres.size()){
            return response;
        }
        List<BaseHourse> sunHourse = houres.subList(pageNumber*pageSize,(pageNumber+1)*pageSize > houres.size() ? houres.size() : (pageNumber+1)*pageSize);
//        Disposable disposable = all.buffer().subscribe(data -> {
//             int total = data.size();
//             totalCount[0] = total;
//             data = data.subList(pageNumber*pageSize,(pageNumber+1)*pageSize > data.size() ? data.size() : (pageNumber+1)*pageSize);
//             houres.addAll(data);
//        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(new ApiResponse(200,"success",sunHourse,
                houres.size(),pageNumber,pageSize)));
    }
}
