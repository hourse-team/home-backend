package com.home.repository;

import com.home.model.BaseHourse;
import com.home.repositoryImpl.ReactivePagingRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Administrator on 2017/8/19.
 */
public interface HourseRepository extends ReactiveMongoRepository<BaseHourse,String>{

    <T extends BaseHourse> Flux<T> findByCreateByOrIsPublic(Sort sort, String userId, String state);

    <T extends BaseHourse> Flux<T> findByTypeAndIsDeleted(Pageable pageable,String status,String isDeleted);

    Mono<Long> countByTypeAndIsDeleted(String status,String isDeleted);

    <T extends BaseHourse> Flux<T> findByCreateByAndTypeAndIsDeletedOrIsPublicAndTypeAndIsDeleted(Pageable pageable, String userId,String type,String deleted, String isPublic,String types,String deleteds);

    <T extends BaseHourse> Flux<T> findByCreateByAndTypeAndTitleLikeAndIsDeletedOrIsPublicAndTypeAndTitleLikeAndIsDeleted(
            Pageable pageable,String userId,String type,String title,String deleted, String isPublic,String types,String titles,String deleteds);
    <T extends BaseHourse> Flux<T> findByCreateByOrIsPublic(Pageable pageable, Example example,String createBy,String ispublic);

    Mono<Long> countByCreateByAndTypeAndIsDeletedOrIsPublicAndTypeAndIsDeleted(String userId,String type,String deleted, String isPublic,String types,String deleteds);

    Mono<Long> countByCreateByAndTypeAndTitleLikeAndIsDeletedOrIsPublicAndTypeAndTitleLikeAndIsDeleted(
            String userId,String type,String title,String deleted, String isPublic,String types,String titles,String deleteds);

    <T extends BaseHourse> Flux<T> findByIsDeletedAndType(Pageable pageable,String deleted,String type);

    <T extends BaseHourse> Flux<T> findByIsDeletedAndTypeAndTitleLike(Pageable pageable,String deleted,String type,String title);

    Mono<Long> countByIsDeletedAndType(String deleted,String type);

    Mono<Long> countByIsDeletedAndTypeAndTitleLike(String deleted,String type,String title);
}
