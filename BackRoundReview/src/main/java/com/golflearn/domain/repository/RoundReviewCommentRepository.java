package com.golflearn.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.golflearn.domain.entity.RoundReviewCommentEntity;

public interface RoundReviewCommentRepository extends JpaRepository<RoundReviewCommentEntity, Long> {
	@Query(value="DELETE FROM round_review_comment "
			+ "WHERE round_review_cmt_parent_no=?1"
			,nativeQuery = true)
	void deleteComment(Long roundReviewCmtNo);
	
//	@Query()
//	List<RoundReviewCommentEntity> findComments(Long roundReviewBoardNo);
}
