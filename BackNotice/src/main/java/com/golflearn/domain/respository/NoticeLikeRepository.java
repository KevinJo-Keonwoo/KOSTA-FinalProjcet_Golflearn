package com.golflearn.domain.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golflearn.domain.entity.NoticeLikeEntity;

public interface NoticeLikeRepository extends JpaRepository<NoticeLikeEntity, Long> {
//	List <NoticeLikeEntity> findByNoticeBoardEntity_noticeBoardNo(Long noticeBoardNo);
}
