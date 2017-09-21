package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface PagingHourseRepository extends PagingAndSortingRepository<BaseHourse,String> {

    <T extends BaseHourse> Page<T> findByTypeAndIsDeleted(Pageable pageable, String type, String isDeleted);
}
