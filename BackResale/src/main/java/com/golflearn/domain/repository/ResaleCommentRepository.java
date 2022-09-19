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
	public void deleteReComment(Long resaleCmtParentNo);
	

	/**
	 * 대댓글 수 조회
	 * @param resaleCmtParentNo
	 * @return
	 */
	@Query(value = "SELECT SUM(COUNT(resale_cmt_no)) \r\n"
					+ "FROM resale_comment"
					+ "WHERE resale_cmt_parent_no = ?1 "
					+ "GROUP BY resale_cmt_no",nativeQuery = true)
	public Integer findReCommentCnt(Long resaleCmtNo);
	
//	/**
//	 * 부모 댓글 수 조회
//	 * @param resaleBoardNo
//	 * @return
//	 */
//	@Query(value="SELECT SUM(COUNT(resale_cmt_no))"
//			+ "FROM resale_comment WHERE resale_cmt_parent_no =0 AND resale_board_no = ?1 \r\n"
//			+ "GROUP BY resale_cmt_no", nativeQuery=true)
//	public Integer findParentCmtCnt(Long resaleBoardNo);
//	
//	/**
//	 * 원글에 댓글번호 있는지 조회
//	 * @param resaleCmtParentNo
//	 * @return
//	 */
//	@Query(value = "SELECT sum(count(resale_cmt_no))\r\n"
//			+ "FROM resale_comment WHERE resale_board_no = ?1 and resale_cmt_parent_no = 0 \r\n"
//			+ "GROUP BY resale_cmt_no" , nativeQuery=true)
//	public Integer findParentCmtNo(Long resaleCmtParentNo);
}