package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golflearn.domain.entity.QnACommentEntity;

public interface QnACommentRepository extends JpaRepository<QnACommentEntity, Long> {
	
}
