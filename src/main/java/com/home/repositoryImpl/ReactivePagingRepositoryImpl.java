package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public class ReactivePagingRepositoryImpl<T,ID> implements ReactivePagingRepository<T,ID> {

    private static final Logger logger = LoggerFactory.getLogger(ReactivePagingRepositoryImpl.class);

    @Autowired
    PagingHourseRepository pagingHourseRepository;

    @Override
    public <T1 extends BaseHourse> Flux<T1> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted) {
        Page<T1> page = pagingHourseRepository.findByTypeAndIsDeleted(pageable,status,isDeleted);
        logger.info(""+page.getTotalElements());
        return Flux.fromStream(page.getContent().stream());
    }
}
