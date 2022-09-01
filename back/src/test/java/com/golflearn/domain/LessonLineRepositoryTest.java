package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;

@SpringBootTest
class LessonLineRepositoryTest {
	@Autowired
	private LessonLineRepository repository;
	
	@Test
	void testSelectById() throws FindException{
		LessonLine ll = new LessonLine();
		ll.setLsnLineNo(4);
		LessonLine ll2 = new LessonLine();
		ll2.setLsnLineNo(5);
		List<LessonLine> testList = new ArrayList<LessonLine>();
		String userId = "jokw@gmail.com";
		List<LessonLine> list = repository.selectById(userId);
		testList.add(ll);
		testList.add(ll2);
		
		assertTrue(list.get(0).getLsnLineNo() == 4);
	}
	@Test
	void testSelectByProId() throws FindException{
		String proId = "ohpro@gmail.com";
		List<Lesson> list = repository.selectByProdId(proId);
		assertTrue(list.size() == 3);
	}
//	@Test
//	void testType() throws FindException{
//		String userId = "jokw@gmail.com";
//		int type = repository.selectTypeById(userId);
//		assertTrue(type == 0);
//	}
}
