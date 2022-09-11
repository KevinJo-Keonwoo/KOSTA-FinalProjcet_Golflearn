package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golflearn.domain.entity.MeetCategoryEntity;
public interface MeetCategoryRepository extends JpaRepository<MeetCategoryEntity, Long>{

}
