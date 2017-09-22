package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface ReactivePagingRepository<T,ID>{

//    <T extends BaseHourse> Mono<Page<T>> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted);
//
//    <T extends BaseHourse> Mono<Page<T>> findCreateByAndTitleLikeOrIsPublic(Pageable pageable,String userId,String title,String isPublic);
}
