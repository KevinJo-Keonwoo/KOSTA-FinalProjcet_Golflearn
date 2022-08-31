package com.golflearn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.repository.LessonHistoryRepository;
import com.golflearn.domain.repository.LessonRepository;
import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;

@Service(value="lessonService")
public class LessonService {
	@Autowired
	private LessonHistoryRepository lsnHistoryRepository;
	
	@Autowired
	private LessonRepository lsnRepository;
	
	public List<LessonLine> viewLessonHistory(int lsnNo) throws FindException{
		return lsnHistoryRepository.selectLessonHistoryByLsnNo(lsnNo);
	}
	
	public List<Lesson> viewMain() throws FindException {
		return lsnRepository.selectAll();
	}
	
//	public List<Lesson> viewSelectedLessons() throws FindException {
//		return lsnRepository.selectSidogu();
//	}
}
