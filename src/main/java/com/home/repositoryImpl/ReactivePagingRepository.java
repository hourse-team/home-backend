package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@NoRepositoryBean
public interface ReactivePagingRepository<T,ID>{

    <T extends BaseHourse> Mono<Tuple2<List<T>,Long>> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted);
}
