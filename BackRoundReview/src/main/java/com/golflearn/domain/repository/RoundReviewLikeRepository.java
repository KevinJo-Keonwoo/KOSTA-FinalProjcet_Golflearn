package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.golflearn.domain.entity.RoundReviewLikeEntity;

public interface RoundReviewLikeRepository extends JpaRepository<RoundReviewLikeEntity, Long> {
	/**
	 * 좋아요 삭제
	 * @param roundReviewBoardNo
	 */
	@Modifying
	@Query(value = "DELETE FROM round_review_like "
			+ "WHERE round_review_board_no= ?1 AND user_nickname = ?2"
			,nativeQuery = true)
	void deleteLike(Long roundReviewBoardNo, String userNickname);
}
