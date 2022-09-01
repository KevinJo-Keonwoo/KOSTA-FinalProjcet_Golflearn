package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golflearn.domain.entity.QnAComment;

public interface QnACommentRepository extends JpaRepository<QnAComment, Long> {

}
