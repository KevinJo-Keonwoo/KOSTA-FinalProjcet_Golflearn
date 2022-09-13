package com.golflearn.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.golflearn.domain.entity.ResaleBoardEntity;

public interface ResaleBoardRepository extends JpaRepository<ResaleBoardEntity, Long> {
	// 첫번째 인자 : ResaleBoard
	// 두번째 인자 : pk의 자료형
	Page<ResaleBoardEntity> findAll(Pageable pageable); //Pageable -> 페이지 처리를 도와주는
	
	/**
	 * 페이지별 게시물 목록 조회 (최신순)
	 * @param startRow
	 * @param endRow
	 * @return
	 */
	@Query(value = "SELECT *\r\n"
				+ "FROM (SELECT rownum r, a.*\r\n"
				+ 		" FROM (SELECT * \r\n"
				+ 				"FROM resale_board\r\n"
				+ 				"ORDER BY resale_board_no DESC\r\n"
				+ 				") a\r\n"
				+ 		")\r\n"
				+ "WHERE r BETWEEN ?1 AND ?2",
				 nativeQuery= true) //startrow, endrow
	public List<ResaleBoardEntity>findByPage(int startRow, int endRow);

	/**
	 * 게시글 상세 조회 (댓글 조회) - findById로 사용
	 * @param resaleBoardNo
	 * @return
	 */
//	@Query(value = "SELECT rb.*, rc.* "
//				 + "FROM resale_board rb LEFT JOIN resale_comment rc ON (rb.resale_board_no = rc.resale_board_no)"
//				 + "WHERE rb.resale_board_no = ?1", nativeQuery= true)
//	public ResaleBoardEntity findDetail(Long resaleBoardNo);
	
	/**
	 * 댓글, 대댓글 삭제 - cascade속성으로 한번에 삭제
	 * @param resaleBoardNo
	 */
//	@Modifying
//	@Query(value = "DELETE FROM resale_comment WHERE resale_board_no= ?1", nativeQuery =true)
//	public void deleteComments(Long resaleBoardNo);
//	
	/**
	 * 좋아요 삭제 - cascade속성으로 한번에 삭제
	 * @param resaleBoardNo
	 */
//	@Modifying
//	@Query(value = "DELETE FROM resale_like WHERE resale_board_no =?1", nativeQuery = true)
//	public void deleteLike(Long resaleBoardNo);
	
	/**
	 * 검색어로 검색
	 * @param word
	 * @param startRow
	 * @param endRow
	 * @return
	 */
//	@Query(value = "SELECT * FROM (SELECT rownum r, a.* "
//			+ "FROM (SELECT * FROM resale_board "
//					+ "WHERE resale_board_title LIKE %?1% "
//					+ "OR resale_board_content LIKE %?1% "
//					+ "OR user_nickname LIKE %?1% "
//					+ "ORDER BY resale_board_no DESC) a ) WHERE r BETWEEN ?2 AND ?3"
//			, 
//			nativeQuery=true)
//	public List<ResaleBoardEntity> findByWord(String word, int startRow, int endRow);
	
	
	@Query(value = "SELECT * FROM resale_board "
			+ "WHERE resale_board_title LIKE %?1% "
			+ "OR resale_board_content LIKE %?1% "
			+ "OR user_nickname LIKE %?1% "
			+ "ORDER BY resale_board_no DESC", nativeQuery = true)
	public Page<ResaleBoardEntity> findByWord(String word, Pageable pageable);
	
	/**
	 * 대댓글 수를 조회한다
	 * @param resaleCmtParentNo
	 * @return
	 */
	@Query(value = "SELECT SUM(COUNT(resale_cmt_no))\r\n"
			+ "FROM resale_comment WHERE resale_cmt_parent_no = ?1 "
			+ "GROUP BY resale_cmt_no", nativeQuery=true)
	public int findCmtCnt(Long resaleCmtParentNo); 
	
	
	/**
	 * 검색어로 검색한 결과 행 수를 조회
	 * @param word
	 * @return
	 */
	@Query(value="SELECT count(*) FROM resale_board "
			+ "WHERE resale_board_title LIKE %?1% "
			+ "OR resale_board_content LIKE %?1% "
			+ "OR user_nickname LIKE %?1% "
			+ "ORDER BY resale_board_no DESC", nativeQuery=true)
	public int findCountByWord(String word);
}