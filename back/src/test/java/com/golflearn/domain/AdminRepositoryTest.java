package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
@SpringBootTest
class AdminRepositoryTest {
	@Autowired
	AdminRepository adminRepo;
	@Autowired
	LessonRepository lessonRepo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
//	@Test 
//	public void testSelectByLsnStatus() throws FindException{
//		int expectedSize = 2; // lsnStatus 1의 총행수는 7행이다. 1페이지별 5건씩 보면서 2페이지의 행수를 예상
//		int lsnStatus = 1;
//		int currentPage = 2;
//		int cntPerPage = 5;
//		List<Lesson> list = adminRepo.selectByLsnStatus(lsnStatus, currentPage, cntPerPage);
//		assertEquals(expectedSize, list.size());
//	}
//	
//	@Test
//	public void testSelectCntByLsnStatus() throws FindException{
//		int expectedCnt = 2;
//		int cnt = adminRepo.selectCntByLsnStatus(0);
//		assertEquals(expectedCnt, cnt);		
//	}
//	
////	@Transactional
//	@Test
//	public void testModifyLsnStatus() throws ModifyException, FindException{
//	//레슨 승인/반려 테스트
//		int lsnNo = 15;
//		Lesson l = lessonRepo.selectByLsnNo(lsnNo);
//		assertNotNull(l);
//		int beforeLsnStatus = l.getLsnStatus();
//
//		Lesson l2 = new Lesson();
//
////		int expectedLsnStatus = 1; //승인할 경우
//		int expectedLsnStatus = 3; //반려할 경우
//		String expectedLsnRjtReason = "반려사유입니다";
//
//		l2.setLsnRjtReason(expectedLsnRjtReason);
//		l2.setLsnNo(lsnNo);
//		l2.setLsnStatus(expectedLsnStatus);
//		adminRepo.modifyLsnStatus(l2);
//
//		Lesson l3 = lessonRepo.selectByLsnNo(lsnNo);
//	}
//	
//	@Test
//	public void testselectLsnRjtReason() throws FindException{
//		String expectedReason = "반려사유입니다";
//		String reason = adminRepo.selectLsnRjtReason(15);
//		assertEquals(expectedReason, reason);	
//	}
	
	@Test
	public void testSelectByUserType()throws FindException {
		int currentPage = 1;
		int cntPerPage = 5;
		int userType = 0;
		
		int expectedSize = 5;
		
		List<UserInfo> list = adminRepo.selectByUserType(currentPage, cntPerPage, userType);
	 
		assertEquals(expectedSize, list.size());
		
	}
	
}
