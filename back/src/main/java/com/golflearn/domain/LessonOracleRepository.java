package com.golflearn.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonClassification;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
@Repository(value = "lessonOracleRepository")
public class LessonOracleRepository implements LessonRepository {
	@Autowired 
	private SqlSessionFactory sqlSessionFactory;
	
	//레슨 상세보기 : 레슨정보와 레슨후기들을 반환한다
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
	
	//레슨승인요청하기 : 레슨의 레슨분류정보 외 정보를 INSERT
	@Override
	public void insertLsnInfo(Lesson lesson) throws AddException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			session.insert("com.golflearn.mapper.LessonMapper.insertLsnInfo", lesson);
		}catch(Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}
@Override
	public void insertLsnClassification(Lesson lesson) throws AddException {
		SqlSession session = null;
		try {//다중insert
				session = sqlSessionFactory.openSession();
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
	
	@Override
	public List<Lesson> selectAll() throws FindException {
		SqlSession session = null;
		List<Lesson> lessons = null;
		try {
			session = sqlSessionFactory.openSession();
			lessons = session.selectList("com.golflearn.mapper.LessonMapper.selectAll");
			if (lessons == null) {
				throw new FindException("레슨이 없습니다.");
			}
			return lessons;
		} catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public List<Lesson> selectSidogu(int[] locNoArr) throws FindException {
		SqlSession session = null;
		
		try {
			session = sqlSessionFactory.openSession();
			List<Lesson> selectedLessons = session.selectList("com.golflearn.mapper.LessonMapper.selectSidogu", locNoArr);
			if (selectedLessons == null) {
				throw new FindException("레슨이 없습니다.");
			}
			return selectedLessons;
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