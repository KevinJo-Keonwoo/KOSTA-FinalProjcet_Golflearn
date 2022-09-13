package com.golflearn.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.golflearn.domain.entity.NoticeCommentEntity;
import com.golflearn.domain.entity.NoticeLikeEntity;
import com.golflearn.domain.respository.NoticeCommentRepository;
import com.golflearn.domain.respository.NoticeLikeRepository;

@SpringBootTest
class NoticeCommentRepositoryTest {
	@Autowired
	private NoticeCommentRepository cmtRepository;
	
	@Autowired
	private NoticeLikeRepository likeRepository;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	@Test
	void testFindByNoticeCommentByNoticeBoardNo() {
		Long noticeBoardNo = 2L;
		Optional<NoticeCommentEntity> cmtList = cmtRepository.findById(noticeBoardNo);
	
	}

}
