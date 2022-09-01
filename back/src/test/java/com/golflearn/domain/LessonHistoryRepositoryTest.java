package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.LessonHistoryRepository;
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
