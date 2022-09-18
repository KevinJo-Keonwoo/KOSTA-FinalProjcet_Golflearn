package com.golflearn.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetBoardRepository extends JpaRepository<MeetBoard, Long>{
//	List<MeetMember> findAll(org.springframework.data.domain.Pageable paging);
	
	@Query(value ="SELECT *\r\n"
			+ "FROM (SELECT rownum r, a.*\r\n"
			+ "		  FROM (SELECT * \r\n"
			+ "						FROM meet_board mb JOIN meet_category mc ON mc.meet_ctg_no = mb.meet_ctg_no\r\n"
			+ "						ORDER BY meet_board_no DESC\r\n"
			+ "			   	  ) a\r\n"
			+ "			)\r\n"
			+ "WHERE r BETWEEN ?1 AND ?2"
			,nativeQuery = true)
	List<MeetBoard> findByPage(int startRow, int endRow);
	//모든 목록을 페이징처리하여 최신순으로 보여준다
	
	
	@Query(value ="SELECT *\r\n"
			+ "FROM (SELECT rownum r, a.*\r\n"
			+ "		  FROM (SELECT * \r\n"
			+ "						FROM meet_board mb JOIN meet_category mc ON mc.meet_ctg_no = mb.meet_ctg_no\r\n"
			+ "						WHERE meet_board_status = ?1 \r\n"
			+ "						ORDER BY meet_board_no DESC\r\n"
			+ "			   	  ) a\r\n"
			+ "			)\r\n"
			+ "WHERE r BETWEEN ?2 AND ?3"
			,nativeQuery = true)
	List<MeetBoard> findByPageByStatus(Long meetBoardStatus, int startRow, int endRow);
	//모집상태별로 필터링하여 최신순으로 보여준다
	
	
	@Query(value ="SELECT *\r\n"
			+ "FROM (SELECT rownum r, a.*\r\n"
			+ "		  FROM (SELECT * \r\n"
			+ "						FROM meet_board mb JOIN meet_category mc ON mc.meet_ctg_no = mb.meet_ctg_no\r\n"
			+ "						 WHERE meet_board_title like %?1%\r\n"
			+ "						ORDER BY meet_board_no DESC\r\n"
			+ "			   	  ) a\r\n"
			+ "			)\r\n"
			+ "WHERE r BETWEEN ?2 AND ?3"
			,nativeQuery = true)
	List<MeetBoard> findByWordAndPage(String word, int startRow, int endRow);
	//검색어가 제목에 포함된 모임글의 목록을 불러온다
	
	@Query(value ="DELETE FROM meet_member\r\n"
			+ "WHERE meet_board_no = ?1 AND user_nickname = ?2"
			,nativeQuery = true)
	void deleteByIdAndUserNickName(Long meetBoardNo, String UserNickname);
	//모임글의 모임참가자목록에서 삭제된다
	
	
	
}
