package com.golflearn.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetMemberRepository extends JpaRepository<MeetMember, Long>{
//	List<MeetMember> findAll(org.springframework.data.domain.Pageable paging);

	@Query(value ="DELETE FROM meet_member\r\n"
			+ "WHERE meet_board_no = ?1"
			,nativeQuery = true)
	void DeleteByBoardNo(Long meetBoardNo);
	//모임글 삭제시 모임글의 모임참여자를 삭제한다
	
	@Query(value = "DELETE FROM meet_member"
			+ "WHERE meet_board_no = ?1 AND user_nickname = ?2"
			,nativeQuery = true)
	void DeleteByIdAndUserNickName(Long meetBoardNo, String UserNickname);
	//회원이 모임에서 나갈시 모임참여자 목록에서 삭제한다
	
	
	
}
