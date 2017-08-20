package com.home.repository;

import com.home.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface UserRepository extends ReactiveMongoRepository<User,String> {

    Mono<User> findByUsernameAndPassword(String username,String password);
}
