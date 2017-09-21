package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;

@NoRepositoryBean
public interface ReactivePagingRepository<T,ID>{

    <T extends BaseHourse> Flux<T> findByTypeAndIsDeleted(Pageable pageable, String status, String isDeleted);
}
