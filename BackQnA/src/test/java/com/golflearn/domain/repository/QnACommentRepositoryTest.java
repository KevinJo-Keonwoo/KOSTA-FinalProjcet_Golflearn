package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.golflearn.domain.entity.QnABoardEntity;
import com.golflearn.domain.entity.QnACommentEntity;

@SpringBootTest
public class QnACommentRepositoryTest {

	@Autowired
	private QnABoardRepository boardRepo;
	@Autowired
	private QnACommentRepository commentRepo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	@Transactional
	//댓글 수정
	void testUpdateComment() {
		Optional<QnABoardEntity> optB = boardRepo.findById(1L);
		assertTrue(optB.isPresent());
		Optional<QnACommentEntity> optB2 = commentRepo.findById(1L);
		assertTrue(optB2.isPresent());
		
		QnABoardEntity b = optB.get();
		QnACommentEntity c = new QnACommentEntity();
		c.setBoard(b);
		c.setCommentNo(b.getBoardNo());
		c.setQnaCmtContent("c_content1");
		c.setUserNickname("c_nickname1");
		c.setQnaCmtDt(new Date(System.currentTimeMillis()));
		commentRepo.save(c);
	}
	
	@Test
	@Transactional
	//댓글추가
	void testAddComment() {
		Optional<QnABoardEntity> optB = boardRepo.findById(26L);
		assertTrue(optB.isPresent());
		Optional<QnACommentEntity> optB2 = commentRepo.findById(26L);
		assertFalse(optB2.isPresent());
		
		QnABoardEntity b = optB.get();
		QnACommentEntity c = new QnACommentEntity();
		c.setBoard(b);
//		c.setCommentNo(26L);
		c.setQnaCmtContent("c_content1");
		c.setUserNickname("c_nickname1");
		c.setQnaCmtDt(new Date(System.currentTimeMillis()));
		commentRepo.save(c);
	}
	
	@Test
	//댓글 삭제하기
	void testDeleteById() {
		Long commentNo = 71L;
		commentRepo.deleteById(commentNo);
	}
}
