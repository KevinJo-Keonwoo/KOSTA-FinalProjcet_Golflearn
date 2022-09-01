package com.golflearn.domain;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;

@Repository(value="lessonHistoryOracleRepository")
public class LessonHistoryOracleRepository implements LessonHistoryRepository {
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	public List<LessonLine> selectLessonHistoryByLsnNo(int lsnNo) throws FindException {
		SqlSession session = null;
		List<LessonLine> lessonLines = null;
		try {
			session = sqlSessionFactory.openSession();
			lessonLines = session.selectList("com.golflearn.mapper.LessonHistoryMapper.selectLessonHistoryByLsnNo", lsnNo);
			System.out.println(lessonLines);
			if (lessonLines == null) {
				throw new FindException("수강생의 레슨내역이 없습니다.");
			}
			return lessonLines;
		} catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
}
