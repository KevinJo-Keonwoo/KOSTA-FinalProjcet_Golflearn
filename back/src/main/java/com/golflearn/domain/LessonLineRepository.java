package com.golflearn.domain;

import java.util.List;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;

public interface LessonLineRepository {
	/**
	 * 세션의 유저아이디(수강생)에 해당하는 레슨목록을 검색한다 (마이페이지)
	 * @param userId 로그인된 유저 아이디
	 * @return
	 * @throws FindException
	 */
	List<LessonLine> selectById(String userId) throws FindException;
	
	/**
	 * 세션의 유저아이디(프로)에 해당하는 레슨목록을 검색한다 (마이프로페이지)
	 * @param userProId 로그인된 유저 아이디(프로)
	 * @return
	 * @throws FindException
	 */
	List<Lesson> selectByProdId(String userProId) throws FindException;
	
//	/**
//	 * 세션에 로그인된 유저아이디의 타입을 반환한다 (수강생인지 프로인지)
//	 * @param userId 로그인된 유저 아이디
//	 * @return
//	 * @throws FindException
//	 */
//	int selectTypeById(String userId) throws FindException;
	
	
	
	
}
