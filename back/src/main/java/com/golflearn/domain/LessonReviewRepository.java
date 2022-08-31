package com.golflearn.domain;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.LessonReview;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

public interface LessonReviewRepository {
	/**
	 * 수강후기 작성 페이지 로딩 시 후기를 작성하려는 레슨의 제목을 보여준다 
	 * @param lessonLineNo
	 * @return
	 * @throws FindException
	 */
	Lesson selectTitleByLsnLineNo(int lsnLineNo) throws FindException;
	
	/**
	 * 수강후기 수정 페이지 로딩 시 기존 작성한 후기를 불러오기 
	 * @param lessonLineNo
	 * @return
	 * @throws FindException
	 */
	LessonLine selectReviewByLsnLineNo(int lsnLineNo) throws FindException;
	
	/**
	 * 리뷰 작성 클릭 시 리뷰를 삽입해 줌
	 * @param lnsLineNo
	 * @param myStarScore
	 * @param review
	 */
	void insertReview(LessonReview lsnReview) throws AddException;
	
	/**
	 * 리뷰 수정 클릭 시 기존 리뷰를 새로운 리뷰로 업데이트 함 
	 * @param review
	 * @param myStarScore
	 * @param lsnLineNo
	 */
	void modifyReview(LessonReview lsnReview) throws ModifyException;
}
	
