package com.golflearn.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NoticeBoardRepository extends CrudRepository<NoticeBoard, Long> {
	List<NoticeBoard> findAll(org.springframework.data.domain.Pageable paging);
	
	@Query(value="SELECT *\n"
				+ "FROM (SELECT rownum r, a.*\n"
				+ "		  FROM (SELECT notice_board_no, notice_board_title, notice_board_dt, user_nickname, \n"
				+ "					   notice_board_view_cnt, notice_board_like_cnt, notice_board_cmt_cnt\n"
				+ "				FROM notice_board nb\n"
				+ "				ORDER BY notice_board_no DESC\n"
				+ "			   	) a\n"
				+ "		)\n"
				+ "WHERE r BETWEEN ? AND ?")
	List<NoticeBoard> findByPage(int startRow, int endRow);
}
