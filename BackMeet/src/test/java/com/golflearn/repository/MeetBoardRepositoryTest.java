package com.golflearn.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.entity.MeetBoardEntity;
import com.golflearn.domain.entity.MeetCategoryEntity;
import com.golflearn.domain.repository.MeetBoardRepository;
import com.golflearn.domain.repository.MeetCategoryRepository;
import com.golflearn.domain.repository.MeetMemberRepository;


@SpringBootTest
class MeetBoardRepositoryTest {
	@Autowired
	MeetBoardRepository meetBoardRepo;
	
	@Autowired
	MeetMemberRepository meetMemberRepo;
	
	@Autowired
	MeetCategoryRepository meetCategoryRepo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	// 모임글번호의 모임글을 조회한다
	@Test
	void testFindById() {
		Long meetBoardNo = 10L;

		Optional<MeetBoardEntity> optM = meetBoardRepo.findById(meetBoardNo);
		assert (optM.isPresent());

	}
	
	//모임글 목록을 페이징 처리하여 최신순으로 보여준다
	@Test
	void testFindByPage() {
		int currentPage = 1;
		int cntPerPage = 5;
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage + 1;
		List<MeetBoardEntity> list = meetBoardRepo.findByPage(startRow, endRow);
		list.forEach((m)->{
			logger.error(m.toString());
		});
	}
	
	//모집상태별로 필터링하여 최신순으로 보여준다
	@Test
	void testFindByStatusAndPage() {
		Long meetBoardStatus = 1L;
		int currentPage = 1;
		int cntPerPage = 5;
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage + 1;
		List<MeetBoardEntity> list = meetBoardRepo.findByStatusAndPage(meetBoardStatus, startRow, endRow);
		list.forEach((m)->{
			logger.error(m.toString());
		});
	}

	//검색어가 제목에 포함된 모임글의 목록을 불러온다
	@Test
	void testFindByWordAndPage(){
		String word = "오전";
		int currentPage = 1;
		int cntPerPage = 5;
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage + 1;
		List<MeetBoardEntity> list = meetBoardRepo.findByWordAndPage(word, startRow, endRow);
		list.forEach((m)->{
			logger.error(m.toString());
		});
	}
	
	//조회수가 1증가한다
	@Test
	void testUpdateViewCount() {
		Long meetBoardNo = 2L;
		Optional<MeetBoardEntity> optM = meetBoardRepo.findById(meetBoardNo);
		optM.ifPresent((m)->{
			logger.error(m.toString());
			long oldViewCount = m.getMeetBoardViewCnt();
			long newViewCount = oldViewCount+1;
			m.setMeetBoardViewCnt(newViewCount);
			meetBoardRepo.save(m);
			
			long expectedNewViewCount = newViewCount;
			assertEquals(expectedNewViewCount, meetBoardRepo.findById(meetBoardNo).get().getMeetBoardViewCnt());
		});
	}
	
	@Test
	void testDelete() {
		Long meetBoardNo = 2L;
		//선택된 모임글의 모임참여자를 삭제한다
		meetMemberRepo.DeleteByBoardNo(meetBoardNo);
		
		//선택된 모임글을 삭제한다
		meetBoardRepo.deleteById(meetBoardNo);
	}
	
	//모임글을 수정한다
	@Test
	void testModify() {
		Optional<MeetBoardEntity> optM = meetBoardRepo.findById(4L);//b변수에 DB의 4번글정보 담겨있음
		optM.ifPresent((m)->{//보드타입의 객체 b 찾아오기
			m.setMeetBoardContent("프린스틴 밸리 두분 초대합니다 수정");//기존내용 놔두고 바뀐내용의 멤버변수만 set
			meetBoardRepo.save(m);
		});
	}
	
	//모임글을 작성한다
	@Test
	void testWrite() {
		MeetCategoryEntity ccc = new MeetCategoryEntity();
		ccc.setMeetCtgNo(3L);
		ccc.setMeetCtgTitle("매치");
		
		MeetBoardEntity m = new MeetBoardEntity();
		m.setMeetCategory(ccc);
		m.setMeetBoardContent("골린이들 모이세요 즐거운 주말 보냅시다");
		m.setMeetBoardLocation("분당 오리역");
		m.setMeetBoardTitle("골린이들 모두 모여라");
		m.setMeetBoardMaxCnt(4L);
		m.setUserNickname("딜레이킴");
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		m.setMeetBoardMeetDt(sqlDate);
		
		meetBoardRepo.save(m);
	}	
	
	//특정글에 특정유저가 참여중인지 확인한다
	@Test
	void testExistsMember() {
		String userNickname= "미노";
		Long meetBoardNo = 6L;
		meetMemberRepo.countByUserNicknameMeetBoard(userNickname, meetBoardNo);
	}

}