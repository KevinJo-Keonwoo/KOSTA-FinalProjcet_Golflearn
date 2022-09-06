package com.golflearn.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.golflearn.domain.entity.MeetMemberEntity;

public interface MeetMemberRepository extends JpaRepository<MeetMemberEntity, Long>{

	@Modifying
	@Query(value ="DELETE FROM meet_member\r\n"
			+ "WHERE meet_board_no = ?1"
			,nativeQuery = true)
	void DeleteByBoardNo(Long meetBoardNo);
	//모임글 삭제시 모임글의 모임참여자를 삭제한다
	
	@Modifying
	@Query(value = "DELETE FROM meet_member WHERE meet_board_no = ?1 AND user_nickname = ?2"
			,nativeQuery = true)
	void DeleteByIdAndUserNickName(Long meetBoardNo, String userNickname);
	//회원이 모임에서 나갈시 모임참여자 목록에서 삭제한다

	@Query(value = "select count(*) \r\n"
			+ "from meet_member \r\n"
			+ "where user_nickname = ?1 and meet_board_no = ?2"
			,nativeQuery = true)
	int countByUserNicknameMeetBoard(String userNickname, Long meetBoardNo);
	//모임 중복참여 확인을 위해 닉네임과 모임번호의 행이 있는지 확인한다
}
