package com.golflearn.domain.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.dto.Lesson;
import com.golflearn.exception.FindException;
import com.golflearn.service.LessonService;

@SpringBootTest
class LessonOracleRepositoryTest {
	@Autowired
	private LessonOracleRepository repository;

	
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
