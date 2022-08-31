package com.golflearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.repository.LessonRepository;
import com.golflearn.dto.Lesson;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;

@Service(value="lessonService")
public class LessonService {
	
	@Autowired
	private LessonRepository lessonRepo;
	
	/**
	 * 레슨번호의 레슨을 반환한다
	 * @param lsnNo
	 * @return
	 * @throws FindException
	 */
	public Lesson viewLessonDetail(int lsnNo) throws FindException{
			Lesson l = lessonRepo.selectByLsnNo(lsnNo);
			return l;
	}
	
	/**
	 * 레슨을 승인요청한다
	 * @param lesson
	 * @throws AddException
	 */
	public void addLesson(Lesson lesson) throws AddException{
		//레슨정보를 추가한다
		lessonRepo.insertLsnInfo(lesson);
		
		//레슨분류정보를 추가한다
		lessonRepo.insertLsnClassification(lesson);
	}
	
}
