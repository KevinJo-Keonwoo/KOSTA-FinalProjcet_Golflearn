package com.golflearn.domain;

import java.util.List;

import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;

public interface LessonHistoryRepository {
	
	/**
	 * 레슨번호에 해당하는 레슨히스토리를 불러온다.
	 * @param lsnNo
	 * @return LessonLine
	 * @throws FindException
	 */
	List<LessonLine> selectLessonHistoryByLsnNo(int lsnNo) throws FindException;
	
}
