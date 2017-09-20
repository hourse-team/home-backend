package com.home.repository;

import com.home.model.BaseHourse;
import com.home.model.Hourse;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface HourseRepository extends ReactiveMongoRepository<BaseHourse,String>{

    <T extends BaseHourse> Flux<T> findByCreateByOrIsPublic(Sort sort, String userId, String state);

//    <T extends BaseHourse> Flux<T> findByCreateByOrStateAndTitleLike(Sort sort,String userId,Integer state,String title);

    <T extends BaseHourse> Flux<T> findByTypeAndIsDeleted(Sort sort,String status,String isDeleted);

}
