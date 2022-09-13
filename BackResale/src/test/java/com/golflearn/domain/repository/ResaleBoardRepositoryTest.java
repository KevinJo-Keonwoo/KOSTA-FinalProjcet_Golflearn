package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.golflearn.domain.entity.ResaleBoardEntity;


@SpringBootTest
class ResaleBoardRepositoryTest {

	@Autowired
	private ResaleBoardRepository resaleBoardRepo;
	
	@Autowired
	private ResaleCommentRepository resaleCmtRepo;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	void testFindAll() {
		resaleBoardRepo.findAll();
	}
	
	@Test
	void testFindWord() {
		String word = "골프";
		int currentPage = 1;
		resaleBoardRepo.findByWord(word,PageRequest.of(currentPage, 5));
	}
	
	
	// 페이지별 목록 불러오기
	@Test
	void testFindByPage() {
		int currentPage = 4; // 현재페이지
		int cntPerPage = 5; // 페이지당 보여줄 게시글 수
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage +1;
		List<ResaleBoardEntity> list = resaleBoardRepo.findByPage(startRow, endRow);
		list.forEach((rb) ->{
			logger.error(rb.toString());
		});
	}
	
	// 상세 불러오기 (게시글 번호의 게시물을 조회)
	@Test
	void testFindDetail() {
		Long resaleBoardNo = 1L;
		resaleBoardRepo.findById(resaleBoardNo);
		//		ResaleBoardEntity resaleBoardEntity = resaleBoardRepo.findDetail(resaleBoardNo);
//		assertTrue(resaleBoardEntity != null);
	}	
	
	// 조회수 증가
	@Test
	@Transient
	void testViewCnt() {
		Long resaleBoardNo = 3L;
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo); 
		// isPresent() : boolean타입 
		// ifPresent() : void 타입 . 값을 가지고 있는 지 확인 후 에러처리
		optRb.ifPresent((rb) -> {
			int oldViewCnt = rb.getResaleBoardViewCnt();
			int newViewCnt = oldViewCnt + 1;
			rb.setResaleBoardViewCnt(newViewCnt);
			resaleBoardRepo.save(rb);
			
			int expectedNewViewCnt = newViewCnt;
			assertEquals(expectedNewViewCnt, newViewCnt);
		}); 
	}
	
	// 게시글 등록
	@Test
	@Transactional
	void testWriteBoard() {
		ResaleBoardEntity b = new ResaleBoardEntity();
		b.setResaleBoardTitle("test_title");
		b.setResaleBoardContent("test_content");
		b.setUserNickname("바보");
		resaleBoardRepo.save(b);
		
		logger.error(b.toString());
	}
	
	// 게시글 수정
	@Test
	@Transactional // 실제 DB에 반영되지 않음
	void testModifyBoard() {
		Long ResaleBoardNo =27L;
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(ResaleBoardNo);
		optRb.ifPresent(rb -> {
			rb.setResaleBoardContent("가나다라");
			resaleBoardRepo.save(rb);
		});
	}
	
	// 게시글 삭제
	@Test
//	@Transactional
	void testDeleteBoard() {
		Long resaleBoardNo = 8L;
		Optional<ResaleBoardEntity> optRb = resaleBoardRepo.findById(resaleBoardNo);
		if(optRb.isPresent()) {
			resaleBoardRepo.deleteById(resaleBoardNo);
			
		}
		
//		resaleBoardRepo.deleteComments(resaleBoardNo);
//		resaleBoardRepo.deleteLike(resaleBoardNo);
//		resaleBoardRepo.deleteById(resaleBoardNo);
//		int expectedCmtCnt = 0;
//		assertEquals(expectedCmtCnt, optRb.get().getResaleBoardCmtCnt());
	}
	
	// 게시글 검색
//	@Test
//	void testFindByWord() {
//		String word = "골프";
//		int currentPage = 1;
//		int cntPerPage = 10;
//		int endRow = currentPage * cntPerPage ;
//		int startRow = endRow - cntPerPage +1 ;
//		List<ResaleBoardEntity> list = resaleBoardRepo.findByWord(word, startRow, endRow);
//		list.forEach((rb)->{
//			logger.error(rb.toString());
//		});	
//	}
	
	
}
