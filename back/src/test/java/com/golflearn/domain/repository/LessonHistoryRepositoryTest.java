package com.golflearn.domain.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;

@SpringBootTest
public class LessonHistoryRepositoryTest {

	@Autowired
	private LessonHistoryRepository repository;
	
	@Test
	public void testSelectByLsnNo() throws FindException{
		int lsnNo = 1;
		int expectedSize = 6;
		List<LessonLine> repo = repository.selectLessonHistoryByLsnNo(lsnNo);
		assertTrue(expectedSize == repo.size());
	}

}
