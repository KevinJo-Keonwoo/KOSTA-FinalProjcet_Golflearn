package com.golflearn.domain.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.golflearn.domain.entity.NoticeBoardEntity;
import com.golflearn.dto.NoticeBoardDto;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity, Long> {
	List<NoticeBoardEntity> findAll(); // 모든 공지사항을 불러온다.
	
	@Query(value="SELECT *\n"
				+ "FROM (SELECT rownum r, a.*\n"
				+ "		  FROM (SELECT * \n"
				+ "				FROM notice_board \n"
				+ "				ORDER BY notice_board_no DESC\n"
				+ "			   	) a\n"
				+ "		)\n"
				+ "WHERE r BETWEEN ?1 AND ?2", 
			nativeQuery=true)
	List<NoticeBoardEntity> findByPage(int startRow, int endRow); // 파라미터로 넣어주는 페이지번호에 해당하는 공지사항을 보여준다.
	
	Optional<NoticeBoardEntity> findByNoticeBoardNo(Long noticeBoardNo);

	// 게시글 검색
	@Query(value="SELECT COUNT(*) FROM notice_board WHERE notice_board_title LIKE %?1% OR board_id LIKE %?1%", nativeQuery = true)
	int searchBoard(String word);
	
	/**
	 * 검색어로 검색
	 * @param word
	 * @param startRow
	 * @param endRow
	 * @return
	 */
	@Query(value = "SELECT * FROM (SELECT rownum r, a.* "
			+ "FROM (SELECT * FROM notice_board "
					+ "WHERE notice_board_title LIKE %?1% "
					+ "OR notice_board_content LIKE %?1% "
					+ "OR user_nickname LIKE %?1% "
					+ "ORDER BY notice_board_no DESC) a ) WHERE r BETWEEN ?2 AND ?3"
			, 
			nativeQuery=true)
	public List<NoticeBoardEntity> findByWord(String word, int startRow, int endRow);
	
	/**
	 * 검색어로 검색한 결과 행 수를 조회
	 * @param word
	 * @return
	 */
	@Query(value="SELECT count(*) FROM notice_board "
			+ "WHERE notice_board_title LIKE %?1% "
			+ "OR notice_board_content LIKE %?1% "
			+ "OR user_nickname LIKE %?1% "
			+ "ORDER BY notice_board_no DESC", nativeQuery=true)
	public int findCountByWord(String word);
	
	@Modifying
	@Query(value = "DELETE FROM notice_comment WHERE notice_board_no =?1"
			, nativeQuery = true)
	void deleteReply(Long boardNo);
	
	@Modifying
	@Query(value = "DELETE FROM notice_like WHERE notice_board_no =?1 ", nativeQuery = true)
	void deleteLike(Long noticeBoardNo);
	
	
}
