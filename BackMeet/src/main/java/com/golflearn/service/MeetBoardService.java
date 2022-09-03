package com.golflearnservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.MeetBoardRepository;
import com.golflearn.domain.MeetMemberRepository;

@Service
public class MeetBoardService {
	@Autowired
	private MeetBoardRepository meetBoardRepo;
	
	@Autowired
	private MeetMemberRepository meetMemberRepo;
	
	//--1. 모임글 목록 불러오기 
	
	//--2. 모임 상세 불러오기 
	
	//--3. 모임글 검색하기
	
	//--4. 모임글 작성하기
	
	//--5. 모임글 삭제하기
	
	//--6. 모임글 수정하기
	
	//--7.글 작성자가 모집상태를 수정한다. 
	
	//--8. 모임에 참여하기(작성자가 아닌경우)
	
	//--9. 모임에서 나가기
	
	//--10.모임일자가 지나면 모집상태가 종료된다.
}
