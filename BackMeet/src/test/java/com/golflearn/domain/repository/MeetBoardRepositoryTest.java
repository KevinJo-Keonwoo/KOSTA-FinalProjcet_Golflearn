package com.golflearn.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.MeetBoard;
import com.golflearn.domain.MeetBoardRepository;


@SpringBootTest
class MeetBoardRepositoryTest {
	@Autowired
	MeetBoardRepository meetBoardRepo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	@Transactional//실제 DB에 commit되지 않고 rollback
	void testFindById() {
		Long meetBoardNo = 27L;
		// 모임글번호의 모임글을 조회한다
		Optional<MeetBoard> optM = meetBoardRepo.findById(meetBoardNo);
		assert (optM.isPresent());

	}

}
