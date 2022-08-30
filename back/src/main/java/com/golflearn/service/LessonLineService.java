package com.golflearn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.LessonLineRepository;
import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;

@Service(value = "lessonLineService")
public class LessonLineService {
	@Autowired
	private LessonLineRepository repository;
	
	List<LessonLine> myLessonList(String userId) throws FindException{
		List<LessonLine> list = repository.selectById(userId);
		return list;
	}
	
	List<Lesson> proLessonList(String userId) throws FindException{
		List<Lesson> list = repository.selectByProdId(userId);
		return null; 
	}
	
	int returnUserType(String userId) throws FindException{
		int type = repository.selectTypeById(userId);
		return type;
	}
}
