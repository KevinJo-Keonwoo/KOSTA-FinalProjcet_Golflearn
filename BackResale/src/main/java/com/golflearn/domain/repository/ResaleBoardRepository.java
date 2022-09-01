package com.golflearn.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.golflearn.domain.entity.ResaleBoardEntity;

public interface ResaleBoardRepository extends JpaRepository<ResaleBoardEntity, Long> {
	// 첫번째 인자 : ResaleBoard
	// 두번째 인자 : pk의 자료형
//	List<ResaleBoard> findAll(Pageable paging); //Pageable -> 페이지 처리를 도와주는
	
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
	List<ResaleBoardEntity>findByPage(int startRow, int endRow);

	/**
	 * 게시글 상세 조회 (댓글 조회)
	 * @param resaleBoardNo
	 * @return
	 */
	@Query(value = "SELECT rb.*, rc.* "
				 + "FROM resale_board rb LEFT JOIN resale_comment rc ON (rb.resale_board_no = rc.resale_board_no)"
				 + "WHERE rc.resale_board_no = ?1", nativeQuery= true)
	List<ResaleBoardEntity> findDetail(Long resaleBoardNo);
	
	/**
	 * 댓글, 대댓글 삭제
	 * @param resaleBoardNo
	 */
	@Query(value = "DELETE FROM resale_comment WHERE resale_board_no= ?1", nativeQuery =true)
	void deleteComments(Long resaleBoardNo);
	
	/**
	 * 좋아요 삭제
	 * @param resaleBoardNo
	 */
	@Query(value = "DELETE FROM resale_like WHERE resale_board_no =?1", nativeQuery = true)
	void deleteLike(Long resaleBoardNo);
	
	/**
	 * 검색어로 검색
	 * @param word
	 * @param startRow
	 * @param endRow
	 * @return
	 */
	@Query(value = "SELECT * FROM (SELECT rownum r, a.* "
			+ "FROM (SELECT * FROM resale_board "
					+ "WHERE resale_board_title LIKE %?1% "
					+ "OR resale_board_content LIKE %?1% "
					+ "OR user_nickname LIKE %?1% "
					+ "ORDER BY resale_board_no DESC) a ) WHERE r BETWEEN ?2 AND ?3"
			, 
			nativeQuery=true)
	List<ResaleBoardEntity> findByWord(String word, int startRow, int endRow);
	
}
