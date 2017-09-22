package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

public class ReactivePagingRepositoryImpl<T,ID> implements ReactivePagingRepository<T,ID> {

//    private static final Logger logger = LoggerFactory.getLogger(ReactivePagingRepositoryImpl.class);
//
//    @Autowired
//    PagingHourseRepository pagingHourseRepository;
//
//    @Override
//    public <T1 extends BaseHourse> Mono<Page<T1>> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted) {
//        Page<T1> page = pagingHourseRepository.findByTypeAndIsDeleted(pageable,status,isDeleted);
//        logger.info(""+page.getTotalElements());
//        return Mono.just(page);
//    }
//
//    @Override
//    public <T1 extends BaseHourse> Mono<Page<T1>> findCreateByAndTitleLikeOrIsPublic(Pageable pageable, String userId, String title, String isPublic) {
//        Page<T1> page;
//        if(StringUtils.isEmpty(title)){
//            page = pagingHourseRepository.findByCreateByOrIsPublic(pageable,userId,isPublic);
//        } else {
//            page = pagingHourseRepository.findByCreateByAndTitleLikeOrIsPublicAndTitleLike(pageable,userId,title,isPublic);
//        }
//        return Mono.just(page);
//    }
}
