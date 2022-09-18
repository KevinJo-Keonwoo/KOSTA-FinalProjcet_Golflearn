package com.golflearn.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.golflearn.domain.entity.RoundReviewBoardEntity;
import com.golflearn.domain.repository.RoundReviewBoardRepository;
import com.golflearn.domain.repository.RoundReviewCommentRepository;
import com.golflearn.domain.repository.RoundReviewLikeRepository;
import com.golflearn.exception.FindException;
import com.golflearn.exception.RemoveException;

@SpringBootTest
class RoundReviewBoardRepositoryTest {
	@Autowired
	private RoundReviewBoardRepository repo;
//	private RoundReviewBoardRepository boardRepo;
	private RoundReviewCommentRepository commentRepo;
	private RoundReviewLikeRepository likeRepo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
//	@Test
//	void testLikeddd() {
//		//of(페이지 번호 0부터시작, 몇개의 게시글?, ㅈ어렬
//		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "round_review_board_like_cnt"));
//		Page<RoundReviewBoardEntity> page = repo.testLike("쩐승", pageRequest);
//		List<RoundReviewBoardEntity> entity = page.getContent();
////		long totalElements = page.getTotalElements();
//	}
	
	@Test
	void testFindByOption() throws FindException {
		int CNT_PER_PAGE = 5;
		int currentPage = 1;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
//		int startRow = 1;
//		int endRow = 5;
		
//		List<RoundReviewBoardEntity> list = repo.findListByRecent(startRow, endRow);
//		List<RoundReviewBoardEntity> list = repo.findListByViewCnt(startRow, endRow);
//		List<RoundReviewBoardEntity> list = repo.findListByLike(startRow, endRow);
	}
	
//	@Test
//	void testFindDetail() throws FindException {
//		Long roundReviewBoardNo = 1L;
//		Optional<RoundReviewBoardEntity> entity= repo.findById(roundReviewBoardNo);
//		assertEquals("true", entity.isPresent());
//		List<RoundReviewBoardEntity> list = repo.findDetail(roundReviewBoardNo);
//		assertEquals(entity.getRoundReviewBoardNo(), entity.getRoundReviewBoardNo());
//		assertEquals(entity.getUserNickname(), "쩐승");
//		assertEquals(entity.getRoundReviewComment().getRoundReviewCmtNo(), 1);
//		assertEquals(entity.getRoundReviewComment().getRoundReviewCmtNo(), 2);
//	}
	
//	@Test
//	@Transactional
//	@Rollback(false)
//	void testDeleteComments() throws RemoveException {
//		Long roundReviewBoardNo = 5L;
//		commentRepo.deleteComments(roundReviewBoardNo);
//	}
//	
//	@Test
//	@Transactional
//	void testDeleteRecomment() throws RemoveException {
//		Long roundReviewCmtNo = 9L;
//		commentRepo.deleteRecomment(roundReviewCmtNo);
//	}
//	
//	@Test
//	@Transactional
//	void testDeleteLike() throws RemoveException {
//		Long roundReviewBoardNo = 1L;
//		String userNickname = "dd";
//		likeRepo.deleteLike(roundReviewBoardNo, userNickname);
//	}
	
	@Test
	void testFindWord() throws FindException {
		String word = "제목";
		String content = "내용";
		String nickname = "닉네임";
		
		int CNT_PER_PAGE = 5;
		int currentPage = 1;
		int endRow = currentPage * CNT_PER_PAGE;
		int startRow = endRow - CNT_PER_PAGE + 1;
		
//		List<RoundReviewBoardEntity> list = repo.findByWord(word, startRow, endRow);
//		assertEquals(word, list.get(0).getRoundReviewBoardTitle());
		
//		list.forEach((RoundReviewBoard)->{
//			logger.error(RoundReviewBoard.toString());
//		});
	}

}
