package com.golflearn.domain.repository;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.golflearn.dto.Lesson;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
@Repository
public class LessonOracleRepository implements LessonRepository {
	@Autowired 
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	public Lesson selectByLsnNo(int lsnNo) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			Lesson lesson = session.selectOne("com.golflearn.mapper.LessonMapper.selectByLsnNo", lsnNo);
			
			if (lesson == null) {
				throw new FindException("해당하는 레슨이 없습니다");
			}
			return lesson;
		} catch (FindException e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if (session != null) {
				session.close(); 
			}
		}
	}

	@Override
	public void insertLsnInfo(Lesson lesson) throws AddException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			session.insert("com.golflearn.mapper.LessonMapper.insertLsnInfo", lesson);
			session.insert("com.golflearn.mapper.LessonMapper.insertLsnClassification", lesson);
		}catch(Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	
}
