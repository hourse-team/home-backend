package com.home.repository;

import com.home.model.BaseHourse;
import com.home.repositoryImpl.ReactivePagingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface HourseRepository extends ReactiveMongoRepository<BaseHourse,String>{

    <T extends BaseHourse> Flux<T> findByCreateByOrIsPublic(Sort sort, String userId, String state);

//    <T extends BaseHourse> Flux<T> findByCreateByOrStateAndTitleLike(Sort sort,String userId,Integer state,String title);

    <T extends BaseHourse> Flux<T> findByTypeAndIsDeleted(Sort sort,String status,String isDeleted);

    <T extends BaseHourse> Flux<T> findByCreateByAndTypeOrIsPublicAndType(Sort sort,String userId,String type, String isPublic,String types);

    <T extends BaseHourse> Flux<T> findByCreateByAndTypeAndTitleLikeOrIsPublicAndTypeAndTitleLike(Sort sort,String userId,String type,String title, String isPublic,String types,String titles);
}
