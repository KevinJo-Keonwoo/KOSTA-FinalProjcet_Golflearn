package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.LessonOracleRepository;
import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonClassification;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.service.LessonService;
@SpringBootTest
class LessonRepositoryTest {

	@Autowired
	private LessonRepository repository;
	
	@Autowired
	private LessonService lessonService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Test
	public void testSelectByLsnNo() throws FindException{
		int expectedLsnDays = 30;
		String expectedLsnIntro = "소개1";
		
		int lsnNo = 1;
		Lesson lesson = repository.selectByLsnNo(lsnNo);
		
		assertNotNull(lesson);
		assertEquals(expectedLsnDays, lesson.getLsnDays());
		assertEquals(expectedLsnIntro, lesson.getLsnIntro());
	}
	
//	@Transactional
	@Test
	public void testAddLesson() throws AddException{
		//레슨승인요청하기 테스트
		//sequence때문에 insertLsnClassification, insertLsnInfo 같이 테스트
		String expectedUserId = "ohpro@gmail.com";

		String expectedLocNo = "11160";
		String expectedLsnTitle= "title";
		String expectedLsnLv = "1";
		String expectedLsnIntro = "intro";
		int expectedLsnPrice = 1000;
		int expectedLsnPerTime = 30;
		int expectedLsnDays = 30;
		int expectedLsnCntSum = 10;
		
		Lesson l = new Lesson();
		UserInfo u = new UserInfo();
		u.setUserId(expectedUserId);
		
		l.setUserInfo(u);
		l.setLocNo(expectedLocNo);
		l.setLsnTitle(expectedLsnTitle);
		l.setLsnLv(expectedLsnLv);
		l.setLsnDays(expectedLsnDays);
		l.setLsnIntro(expectedLsnIntro);
		l.setLsnPrice(expectedLsnPrice);
		l.setLsnPerTime(expectedLsnPerTime);
		l.setLsnCntSum(expectedLsnCntSum);
		
		int expectedClubNo = 9;
		int expectedClubNo2 = 8;
		
		List<LessonClassification> lcList = new ArrayList<LessonClassification>();		
		
		LessonClassification lc = new LessonClassification();
		LessonClassification lc2 = new LessonClassification();
		
		lc.setClubNo(expectedClubNo);
		lc2.setClubNo(expectedClubNo2);
		
		lcList.add(lc);
		lcList.add(lc2);
		
		l.setLsnClassifications(lcList);

//		lessonRepo.insertLsnClassification(l);//insertLsnClassification테스트
		
		lessonService.addLesson(l);
    
}
	@Test
	public void testSelectAll() throws FindException {
		int expectedSize = 14;
		List<Lesson> repo = repository.selectAll();
		assertTrue(expectedSize == repo.size());
	}
	
	@Test
	public void testSelectSidogu() throws FindException {
		int[] locNoArr = {11161, 11160, 41111};
		int expectedSize = 12;
		List<Lesson> repo = repository.selectSidogu(locNoArr);
		assertTrue(expectedSize == repo.size());
	}
}
