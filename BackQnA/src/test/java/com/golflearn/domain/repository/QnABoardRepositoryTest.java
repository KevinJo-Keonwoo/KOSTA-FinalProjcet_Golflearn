package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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
class QnABoardRepositoryTest {

	@Autowired
	private QnABoardRepository boardRepo;
	@Autowired
	private QnACommentRepository commentRepo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	@Transactional
	//글 등록
	void testAddBoard() {
		QnABoardEntity b = new QnABoardEntity();
		b.setBoardNo(26L);
		b.setBoardTitle("문의사항 등록 테스트");
		b.setBoardContent("b_content1");
		b.setQnaBoardDt(new Date(System.currentTimeMillis()));
		b.setUserNickname("가나다");
		b.setQnaBoardSecret(1);
		boardRepo.save(b);
	}
	
	@Test
	@Transactional
	//댓글 등록
	void testAddComment() {
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
	//게시판 목록 조회
	void testFindByPage() {
		int currentPage = 1;
		int cntPerPage = 3;
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage + 1;
		List<QnABoardEntity> list = boardRepo.findByPage(startRow, endRow);
		list.forEach((b)->{
			logger.error(b.toString());
		}); 
	}
		
	@Test
	//글 상세보기
	void testFindDetail() {
		Long qnaBoardNo = 1L;
		List<QnABoardEntity> list = boardRepo.findDetail(qnaBoardNo);
		list.forEach((b)->{
			logger.error(b.toString());
		});
	}
	
//	@Test
//	//공개글리스트(필터) 보기
//	void testFindByOpenPost() {
//		int currentPage = 1;
//		int cntPerPage = 3;
//		int endRow = currentPage * cntPerPage;
//		int startRow = endRow - cntPerPage + 1;
//		List<Object[]> list = boardRepo.findByOpenPost(startRow, endRow);
//		List<QnABoardEntity> listEntity = new ArrayList<>();
//		list.forEach((arr)->{
//			logger.error("0:" + arr[0] + ", 1:"+ arr[1]);
//			QnABoardEntity entity = new QnABoardEntity();
//			entity.setBoardNo(   ((BigDecimal)arr[1]).longValue()     );
//			entity.setBoardTitle((String)arr[2]);
//			listEntity.add(entity);
//		});
//		logger.error("entity:" + listEntity);
//	}
	
	@Test
	@Transactional
	//글 수정하기
	void testModifyBoard() {
		QnABoardEntity qboard = new QnABoardEntity();
		qboard.setBoardNo(3L);
		qboard.setBoardTitle("글 수정");
		qboard.setBoardContent("수정");
		boardRepo.save(qboard);
	}
	
	@Test
	@Transactional
	//글 삭제하기
	void testDeleteById() {
		Long boardNo = 3L;
		boardRepo.deleteById(boardNo);
	}
	
	@Test
	//닉네임으로 찾기
	void testFindByNickname( ) {
		String nicknameWord = "데";
		int currentPage = 1;
		int cntPerPage = 5;
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage + 1;
		List<QnABoardEntity> list = boardRepo.findByNickname(nicknameWord, startRow, endRow);
		list.forEach((b)->{
			logger.error(b.toString());
		});
	}
}
