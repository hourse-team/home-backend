package com.home.handler;

import com.home.model.User;
import com.home.repository.UserRepository;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return user.flatMap(use -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(use)))
                .switchIfEmpty(notFound);
    }
}
