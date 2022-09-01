package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golflearn.domain.entity.QnABoard;

public interface QnABoardRepository extends JpaRepository<QnABoard, Long> {

}
