package com.home.repositoryImpl;

import com.home.model.BaseHourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;


@NoRepositoryBean
public interface PagingHourseRepository extends PagingAndSortingRepository<BaseHourse,String> {

    <T extends BaseHourse> Page<T> findByTypeAndIsDeleted(Pageable pageable, String type, String isDeleted);

    <T extends BaseHourse> Page<T> findByCreateByOrIsPublic(Pageable pageable,String userId,String isPublic);

    <T extends BaseHourse> Page<T> findByCreateByAndTitleLikeOrIsPublicAndTitleLike(Pageable pageable,String userId,String title, String isPublic);
}
