package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import com.golflearn.domain.entity.NoticeBoardEntity;
import com.golflearn.domain.entity.NoticeCommentEntity;
import com.golflearn.domain.respository.NoticeBoardRepository;

@SpringBootTest
class NoticeBoardRepositoryTest {
	@Autowired
	private NoticeBoardRepository repository;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	void testFindByPage() {
		int currentPage = 1;
		int cntPerPage = 5;
		
		int endRow = currentPage * cntPerPage;
		int startRow = endRow - cntPerPage +1;
		
		List<NoticeBoardEntity> list = repository.findByPage(startRow, endRow);
		list.forEach((b)->{
			logger.error(b.toString());
		});
	}
	
	@Test
	void testFinfByNoticeBoardNo() {
		Long noticeBoardNo = 2L;
		Optional<NoticeBoardEntity> optB1 = repository.findById(noticeBoardNo);
		assert(optB1.isPresent());
	}
	
	@Transactional
	@Commit
	@Test
	void testDeleteBoard() {
		Long noticeBoardNo = 2L;
		repository.deleteReply(noticeBoardNo);
		repository.deleteLike(noticeBoardNo);
		repository.deleteById(noticeBoardNo);
	}
//	@Test
//	void testFindByNoticeCommentByNoticeBoardNo() {
//		Long noticeBoardNo = 2L;
//		Optional<NoticeCommentEntity> optB1 = repository.findCommentsByNoticeBoardNo(noticeBoardNo);
//		assert(optB1.isPresent());
//	}

}
