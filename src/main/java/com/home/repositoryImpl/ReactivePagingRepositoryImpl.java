package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public class ReactivePagingRepositoryImpl<T,ID> implements ReactivePagingRepository<T,ID> {

    @Autowired
    PagingHourseRepository pagingHourseRepository;

    @Override
    public <T1 extends BaseHourse> Flux<T1> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted) {
        Page<T1> page = pagingHourseRepository.findByTypeAndIsDeleted(pageable,status,isDeleted);
        return Flux.fromStream(page.getContent().stream());
    }
}
