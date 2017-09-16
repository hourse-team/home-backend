package com.home.handler;

import com.home.model.User;
import com.home.repository.UserRepository;
import com.home.vo.ApiResponse;
import com.home.vo.NoPagingResponse;
import com.home.vo.PageRequest;
import com.mongodb.util.JSON;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by Administrator on 2017/8/19.
 */
@Component
public class Userhandler {
    @Autowired
    private UserRepository userRepository;

    @Value("${qiniu.access_key}")
    private String accessKey;

    @Value("${qiniu.secret_key}")
    private String secretKey;

    private static final Logger logger = LoggerFactory.getLogger(Userhandler.class);

    public Mono<ServerResponse> getUser(ServerRequest request){
        User data = request.bodyToMono(User.class).block();
        Mono<User> user = userRepository.findByUsernameAndPassword(data.getUsername(),data.getPassword());
        Mono<ServerResponse> notFound = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(201,"fail",null)));
        return user.flatMap(use -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Access-Control-Allow-Origin","*")
                .body(fromObject(new NoPagingResponse(200,"success",use))))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> index(ServerRequest request){
        Mono<ClassPathResource> cp = Mono.just(new ClassPathResource("templates/index.html"));
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(cp,ClassPathResource.class);
    }

    public Mono<ServerResponse> register(ServerRequest request){
        User newUser = request.bodyToMono(User.class).block();
        if(newUser.getType() == null || (newUser.getType() != 1 && newUser.getType() != 0)){
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(fromObject(new NoPagingResponse(202,"error","未指定用户类型")));
        }
        Mono<User> user = userRepository.findByUsername(newUser.getUsername());
        User use = user.block();
        if(use == null){
           return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(fromObject(new NoPagingResponse(200,"success",userRepository.insert(newUser).block())));
        }
        return user.flatMap(a -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(201,"fail","账号已存在"))));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request){
        String userId = request.pathVariable("userId");
        Mono<Void> data = userRepository.deleteById(userId);
//        Mono<ServerResponse> notFound =  ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(fromObject(new ApiResponse(201,"fail",null)));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",data.block())));
    }

    public Mono<ServerResponse> getAllUser(ServerRequest request){
        PageRequest page = request.bodyToMono(PageRequest.class).block();
        if(page == null){
            page = new PageRequest();
        }
        int end;
        Flux<User> users = userRepository.findAll(Sort.by(new Sort.Order(DESC,"createDate")));
        Long count = users.count().block();
        users.range(page.getPageNumber()*page.getPageSize(),end = (page.getPageNumber()+1)*page.getPageSize() > count ?
                count.intValue() : (page.getPageNumber()+1)*page.getPageSize().intValue());
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",users.collectList().block(),
                        (int) Math.ceil(count.doubleValue()/page.getPageSize()),page.getPageNumber(),page.getPageSize())));
    }

    public Mono<ServerResponse> getUploadToken(ServerRequest request){
        String saveKey = request.queryParam("saveKey").orElse(null);
        String bucket = "dingshengfangchan";
        Auth auth = Auth.create(accessKey, secretKey);

        StringMap policy = new StringMap();
        policy.put("insertOnly", 1);
        policy.putNotEmpty("saveKey", saveKey);

        String token = auth.uploadToken(bucket, null, 3600, policy);
        logger.debug("bucket {}, qiniu upload token: {}", bucket, token);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new NoPagingResponse(200,"success",token)));
    }
    public static void main(String[] args){
            Flux.just(1,2,3,4,5).range(2, 3)
                    .map(i -> "" + i).subscribe(System.out::println);

    }
}
