package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.golflearn.domain.entity.RoundReviewBoardEntity;
import com.golflearn.domain.entity.RoundReviewCommentEntity;
import com.golflearn.domain.repository.RoundReviewBoardRepository;
import com.golflearn.exception.FindException;
import com.golflearn.exception.RemoveException;

@SpringBootTest
class RoundReviewBoardRepositoryTest {
	@Autowired
	private RoundReviewBoardRepository repo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Test
	void testFindByOption() throws FindException {
		int CNT_PER_PAGE = 5;
		int currentPage = 1;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
//		List<RoundReviewBoardEntity> list = repo.findListByRecent(startRow, endRow);
//		List<RoundReviewBoardEntity> list = repo.findListByViewCnt(startRow, endRow);
		List<RoundReviewBoardEntity> list = repo.findListByLike(startRow, endRow);
	}
	
	@Test
	void testFindDetail() throws FindException {
		Long roundReviewBoardNo = 1L;
		Optional<RoundReviewBoardEntity> entity= repo.findById(roundReviewBoardNo);
		assertEquals("true", entity.isPresent());
//		List<RoundReviewBoardEntity> list = repo.findDetail(roundReviewBoardNo);
//		assertEquals(entity.getRoundReviewBoardNo(), entity.getRoundReviewBoardNo());
//		assertEquals(entity.getUserNickname(), "쩐승");
//		assertEquals(entity.getRoundReviewComment().getRoundReviewCmtNo(), 1);
//		assertEquals(entity.getRoundReviewComment().getRoundReviewCmtNo(), 2);
	}
	
	@Test
	@Transactional
//	@Rollback(false)
	void testDeleteComments() throws RemoveException {
		Long roundReviewBoardNo = 5L;
		repo.deleteComments(roundReviewBoardNo);
	}
	
	@Test
	@Transactional
	void testDeleteRecomment() throws RemoveException {
		Long roundReviewCmtNo = 9L;
		repo.deleteRecomment(roundReviewCmtNo);
	}
	
	@Test
	@Transactional
	void testDeleteLike() throws RemoveException {
		Long roundReviewBoardNo = 1L;
		repo.deleteLike(roundReviewBoardNo);
	}
	
	@Test
	void testFindWord() throws FindException {
		String word = "제목";
		String content = "내용";
		String nickname = "닉네임";
		
		int CNT_PER_PAGE = 5;
		Long currentPage = 1L;
		Long endRow = currentPage * CNT_PER_PAGE;
		Long startRow = endRow - CNT_PER_PAGE + 1;
		
		List<RoundReviewBoardEntity> list = repo.findWord(word, startRow, endRow);
		assertEquals(word, list.get(0).getRoundReviewBoardTitle());
		
//		list.forEach((RoundReviewBoard)->{
//			logger.error(RoundReviewBoard.toString());
//		});
	}

}
