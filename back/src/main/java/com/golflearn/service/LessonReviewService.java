package com.golflearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.LessonReviewRepository;
import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.LessonReview;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@Service(value = "lessonReviewService")
public class LessonReviewService {
	@Autowired
	private LessonReviewRepository repository;
	
	public Lesson newReview(int lsnLineNo) throws FindException {
		Lesson lesson = repository.selectTitleByLsnLineNo(lsnLineNo);
		return lesson;
	}
	public LessonLine previousReview(int lsnLineNo) throws FindException {
		LessonLine lessonLine = repository.selectReviewByLsnLineNo(lsnLineNo);
		return lessonLine;
	}
	public void writeReview(LessonReview lsnReview) throws AddException{
		repository.insertReview(lsnReview);
	}
	public void modifyReview(LessonReview lsnReview) throws ModifyException {
		repository.modifyReview(lsnReview);
	}
	
}
