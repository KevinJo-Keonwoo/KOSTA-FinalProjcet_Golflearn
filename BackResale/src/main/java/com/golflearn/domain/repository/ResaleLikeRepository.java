package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golflearn.domain.entity.ResaleCommentEntity;

public interface ResaleLikeRepository extends JpaRepository<ResaleCommentEntity, Long> {

}
