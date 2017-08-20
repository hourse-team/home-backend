package com.home.repository;

import com.home.model.Hourse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface HourseRepository extends ReactiveMongoRepository<Hourse,String>{

    Flux<Hourse> findByUserIdOrState(String userId,Integer state);
}
