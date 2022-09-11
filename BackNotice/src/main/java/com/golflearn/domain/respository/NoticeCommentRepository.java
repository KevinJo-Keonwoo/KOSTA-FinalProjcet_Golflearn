package com.golflearn.domain.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golflearn.domain.entity.NoticeCommentEntity;

public interface NoticeCommentRepository extends JpaRepository<NoticeCommentEntity, Long> {
//	List <NoticeCommentEntity> findByNoticeBoardEntity_noticeBoardNo(Long noticeBoardNo);
}
