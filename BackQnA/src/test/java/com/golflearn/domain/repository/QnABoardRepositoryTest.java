package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.entity.QnABoard;
import com.golflearn.domain.entity.QnAComment;
@SpringBootTest
class QnABoardRepositoryTest {

	@Autowired
	private QnABoardRepository bRepo;
	@Autowired
	private QnACommentRepository cRepo;
	@Test
	void testAddBoard() {
		QnABoard b = new QnABoard();
		b.setBoardTitle("b_title1");
		b.setBoardContent("b_content1");
		b.setQnaBoardDt(new Date(System.currentTimeMillis()));
		b.setQnaBoardSecret(1);
		bRepo.save(b);
	}
	@Test
	void testAddComment() {
		Optional<QnABoard> optB = bRepo.findById(2L);
		assertTrue(optB.isPresent());
		
		QnABoard b = optB.get();
		QnAComment c = new QnAComment();
		c.setBoard(b);
		c.setCNo(b.getBNo());
		c.setQnaCmtContent("c_content1");
		c.setUserNickname("c_nickname1");
		c.setQnaCmtDt(new Date(System.currentTimeMillis()));
		cRepo.save(c);
		
	}

}
