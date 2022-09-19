package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.LessonReview;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@SpringBootTest
class LessonReviewRepositoryTest {
	@Autowired
	private LessonReviewRepository repository;
	
	@Test
	void selectTitleByLsnLineNo() throws FindException{
		int lsnLineNo = 3;
		String expectedTitle = "간결한 백스윙 만들기";
		Lesson lsn = repository.selectTitleByLsnLineNo(lsnLineNo);
		assertTrue(lsn.getLsnLine().getLsnLineNo() == 3);
		assertEquals( expectedTitle,lsn.getLsnTitle());
	}
	@Test
	void testSelectReviewByLsnLineNo() throws FindException{
		int testStar = 4;
		LessonLine ll = repository.selectReviewByLsnLineNo(3);
		assertTrue(ll.getLsnReview().getMyStarScore() == 4);
	}
	@Test
	void testInsertLessonReview() throws AddException{
		LessonReview lr = new LessonReview();
		LessonLine ll = new LessonLine();
		ll.setLsnLineNo(8);
		lr.setLsnLine(ll);
		
		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		lr.setMyStarScore(5);
		lr.setReview("아주좋았어요");
		lr.setReviewDt(sqlDate);
		lr.setReviewEditDt(sqlDate);
		
		
		repository.insertReview(lr);
		assertTrue(lr.getMyStarScore() == 5);
	}
	@Test
	void testModifyLessonReview() throws ModifyException{
		LessonReview lr = new LessonReview();
		LessonLine ll = new LessonLine();
		ll.setLsnLineNo(8);
		lr.setLsnLine(ll);
		lr.setReview("수정된내용");
		lr.setMyStarScore(3);
		
		repository.modifyReview(lr);
		assertEquals(lr.getReview(), "수정된내용");
	} 
}
