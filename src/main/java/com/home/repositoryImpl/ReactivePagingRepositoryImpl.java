package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public class ReactivePagingRepositoryImpl<T,ID> implements ReactivePagingRepository<T,ID> {

    @Autowired
    PagingHourseRepository pagingHourseRepository;

    @Override
    public <T1 extends BaseHourse> Mono<Tuple2<List<T1>,Long>> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted) {
        Page<T1> page = pagingHourseRepository.findByTypeAndIsDeleted(pageable,status,isDeleted);
        return Flux.fromStream(page.getContent().stream()).collectList().zipWith(Mono.just(page.getTotalElements()));
    }
}
