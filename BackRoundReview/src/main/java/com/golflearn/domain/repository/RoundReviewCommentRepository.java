package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.golflearn.domain.entity.RoundReviewComment;

public interface RoundReviewCommentRepository extends JpaRepository<RoundReviewComment, Long> {
	@Query(value="DELETE FROM round_review_comment "
			+ "WHERE round_review_cmt_parent_no=?1"
			,nativeQuery = true)
	void deleteComment(Long roundReviewCmtNo);
}
