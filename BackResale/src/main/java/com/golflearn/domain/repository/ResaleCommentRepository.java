package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.golflearn.domain.entity.ResaleCommentEntity;

public interface ResaleCommentRepository extends CrudRepository<ResaleCommentEntity, Long> {
	/**
	 * 대댓글 삭제
	 * @param resaleCmtParentNo
	 */
	@Modifying
	@Query(value="DELETE FROM resale_comment WHERE resale_cmt_parent_no= ?1", 
			nativeQuery=true)
	void deleteReComment(Long resaleCmtParentNo);
	

	/**
	 * 대댓글 수 조회
	 * @param resaleCmtParentNo
	 * @return
	 */
	@Query(value = "SELECT SUM(COUNT(resale_cmt_no)) "
				+ "FROM resale_comment WHERE resale_cmt_parent_no = ?"
				+ "GROUP BY resale_cmt_no", nativeQuery=true)
	int findReCommentCnt(Long resaleCmtParentNo);
	
}
