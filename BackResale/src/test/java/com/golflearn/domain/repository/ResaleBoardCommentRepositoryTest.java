package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.golflearn.domain.entity.ResaleBoardEntity;
import com.golflearn.domain.entity.ResaleCommentEntity;

@SpringBootTest
class ResaleBoardCommentRepositoryTest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ResaleCommentRepository resaleCommentRepo;
	
	@Autowired
	ResaleBoardRepository resaleBoardRepo;
	
	//댓글 등록
	@Test
	@Transactional
	void testInsertComment() {
		ResaleBoardEntity resaleBoard = new ResaleBoardEntity();
		resaleBoard.setResaleBoardNo(27L);
		
		ResaleCommentEntity resaleComment = new ResaleCommentEntity();
		resaleComment.setResaleBoard(resaleBoard);
		resaleComment.setResaleCmtContent("댓글 등록 테스트"); //27번 글에 대한
		resaleComment.setUserNickname("땡초"); 


		resaleCommentRepo.save(resaleComment);
		
		logger.error("글번호" + resaleComment.getResaleBoard().getResaleBoardNo());
		logger.error("부모댓글번호" + resaleComment.getResaleCmtNo());
		logger.error("작성자" + resaleComment.getUserNickname());
	}
	
	
	//댓글 수 증가 (댓글 작성 시)
	@Test
	void testResaleCmtCnt() {
		Long resaleBoardNo = 27L;
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
		optRb.ifPresent(rb -> {
			int oldCmtCnt = rb.getResaleBoardCmtCnt();
			int newCmtCnt = oldCmtCnt + 1 ;
			rb.setResaleBoardCmtCnt(newCmtCnt);
			resaleBoardRepo.save(rb);
			
			int expectedNewViewCnt = newCmtCnt;
			assertEquals(expectedNewViewCnt, newCmtCnt);
		});
	}
	
	// (대)댓글 수정
	@Test
	void testModifyComment() {
		Long resaleBoarCommentdNo = 12L;
		Optional<ResaleCommentEntity> optRb = resaleCommentRepo.findById(resaleBoarCommentdNo);
		optRb.ifPresent(rb -> {
			rb.setResaleCmtContent("ㅋㅋ");
			
			resaleCommentRepo.save(rb);
			logger.error(rb.toString());
		});
	}
	
	//댓글 삭제
	@Test
//	@Transactional
	void testDeleteComments() {
		Long resaleCommentNo = 106L;
		Long resaleCmtParentNo = resaleCommentNo;

		Optional<ResaleCommentEntity> optRbC = resaleCommentRepo.findById(resaleCommentNo);
		optRbC.ifPresent(rb->{
			resaleCommentRepo.deleteReComment(resaleCmtParentNo); //대댓글 삭제
			resaleCommentRepo.deleteById(resaleCommentNo);
			System.out.println(rb.toString());
//			logger.error(rb.toString());
		});
	}
	
	@Test
	void tesFindCmtCnt() {
		Long resaleCommentNo = 106L;
		Integer cnt = resaleCommentRepo.findReCommentCnt(resaleCommentNo);
		assertEquals(2, cnt);
	}

	@Test
	void testDelete() {
		Long resaleCmtNo = 12L;
		resaleCommentRepo.deleteById(12L);		
	}
	
	// 대댓글 수 조회
	@Test
	void testReCmtCnt() {
		Long resaleCmtParentNo = 28L;
		int cnt = resaleBoardRepo.findCmtCnt(resaleCmtParentNo);
		
		int expectedCnt = 2;
		assertEquals(expectedCnt, cnt);
	}
	
	//대댓글 삭제
	@Test
	@Transactional
	void testDeleteReComment() {
		Long resaleCmtNo = 39L;
		resaleCommentRepo.deleteById(resaleCmtNo);
	}
	
	//댓글 수 감소 (대댓글 삭제 시)
		@Test
		@Transactional
		void testResaleCmtCnt2() {
			Long resaleBoardNo = 27L;
			Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
			optRb.ifPresent(rb -> {
				int oldCmtCnt = rb.getResaleBoardCmtCnt();
				int newCmtCnt = oldCmtCnt - 1 ;
				rb.setResaleBoardCmtCnt(newCmtCnt);
				resaleBoardRepo.save(rb);
				
				int expectedNewViewCnt = newCmtCnt;
				assertEquals(expectedNewViewCnt, newCmtCnt);
				
				logger.error(rb.toString());
			});
		}

//		@Test
//		void testResaleCmt() {
//			Long resaleBoardNo = 28L;
//			int cnt = resaleCommentRepo.findParentCmtCnt(resaleBoardNo);
//			int expectedCnt = 4;
//			assertEquals(expectedCnt, cnt);
//		}
//		
//		@Test
//		void testResaleCmtParent() {
//			Long resaleBoardNo = 28L;
//			Long cmtParentNo = resaleCommentRepo.findParentCmtNo(resaleBoardNo);
//			Long expected = 17L;
//			assertEquals(cmtParentNo, expected);
//		}
}