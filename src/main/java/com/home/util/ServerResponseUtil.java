package com.home.util;

import com.home.model.BaseHourse;
import com.home.vo.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.management.timer.Timer;

import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public class ServerResponseUtil {

    public static Mono<ServerResponse> createResponse(Object data){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(data));
    }

    public static<T> Mono<ServerResponse> createByMono(Mono<T> mono,Class<T> clazz){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(mono, clazz);
    }

    public static Mono<List<BaseHourse>> page(Mono<PageRequest> page,Mono<List<BaseHourse>> list){
        page.subscribe(res -> {
            list.map(data -> data.subList(0,10));
        });
        return list;
    }
}
