package com.home.handler;

import com.home.model.User;
import com.home.repository.UserRepository;
import com.home.vo.ApiResponse;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Created by Administrator on 2017/8/19.
 */
@Component
public class Userhandler {
    @Autowired
    private UserRepository userRepository;

    public Mono<ServerResponse> getUser(ServerRequest request){
        User data = request.bodyToMono(User.class).block();
        Mono<User> user = userRepository.findByUsernameAndPassword(data.getUsername(),data.getPassword());
        Mono<ServerResponse> notFound = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(201,"fail",null)));
        return user.flatMap(use -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success",use))))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> index(ServerRequest request){
        Mono<ClassPathResource> cp = Mono.just(new ClassPathResource("templates/index.html"));
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(cp,ClassPathResource.class);
    }

    public Mono<ServerResponse> register(ServerRequest request){
        User newUser = request.bodyToMono(User.class).block();
        Mono<User> user = userRepository.findByUsername(newUser.getUsername());
        System.out.println(user.block());
        if(user.block() == null){
           return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(fromObject(new ApiResponse(200,"success",userRepository.insert(newUser).block())));
        }
        return user.flatMap(a -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(201,"fail","账号已存在"))));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request){
        String userId = request.pathVariable("userId");
        Mono<Void> data = userRepository.deleteById(userId);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(fromObject(new ApiResponse(200,"success","删除成功")));
    }
}
