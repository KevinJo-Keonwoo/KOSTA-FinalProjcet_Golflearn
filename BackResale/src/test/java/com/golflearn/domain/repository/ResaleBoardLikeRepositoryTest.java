package com.golflearn.domain.repository;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.entity.ResaleBoardEntity;
import com.golflearn.domain.entity.ResaleLikeEntity;

@SpringBootTest
class ResaleBoardLikeRepositoryTest {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ResaleLikeRepository resaleLikeRepo;
	
	@Autowired
	ResaleBoardRepository resaleBoardRepo;
	
	// 좋아요
	@Test
//	@Transactional
	void testInsertLike() {
		ResaleLikeEntity resaleLike = new ResaleLikeEntity();
		ResaleBoardEntity resaleBoard = new ResaleBoardEntity();
		resaleBoard.setResaleBoardNo(27L);
		resaleLike.setResaleBoard(resaleBoard);
		resaleLike.setUserNickname("데빌");
		
		logger.error("닉네임은 " + resaleLike.getUserNickname());
		logger.error("좋아요는 " + resaleLike.getResaleLikeNo());
		logger.error("글번호는 " + resaleLike.getResaleBoard().getResaleBoardNo());
		
		resaleLikeRepo.save(resaleLike);
	}

}