package com.golflearn.domain;

import java.util.List;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonClassification;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;

public interface LessonRepository {
	/**
	 * 레슨번호로 레슨정보와 후기들을 검색한다
	 * @param lsnNo 레슨번호
	 * @return 레슨객체
	 * @throws FindException 레슨번호에 해당하는 상품이 없으면 "레슨이 없습니다" 상세메시지를 갖는 예외가 발생한다
	 */
	public Lesson selectByLsnNo(int lsnNo) throws FindException;
	
	/**
	 * 프로가 레슨을 승인요청한다 : 레슨의 레슨분류정보 외 정보
	 * @param Lesson 레슨정보
	 * @throws AddException
	 */
	void insertLsnInfo(Lesson lesson) throws AddException;

	/**
	 * 프로가 레슨을 승인요청한다 : 레슨의 레슨분류정보 정보
	 * @param lsnClassifications 레슨분류정보들
	 * @throws AddException
	 */
	void insertLsnClassification(Lesson lesson) throws AddException;
  
  /**
	 * 전체레슨을 불러온다.
	 * @return List<Lesson>
	 * @throws FindException
	 */
	List<Lesson> selectAll() throws FindException;
	
	List<Lesson> selectSidogu(int[] locNoArr) throws FindException;
	
}

