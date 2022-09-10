package com.golflearn.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;

@Repository
public class AdminOracleRepository implements AdminRepository {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	//유저 전체보기
	@Override
	public List<UserInfo> selectByUserPage(int currentPage, int cntPerPage) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			Map<String, Integer> map = new HashMap<>();
			int endRow = currentPage * cntPerPage;
			int startRow = endRow - cntPerPage + 1;
			map.put("startRow", startRow);
			map.put("endRow", endRow);

			List<UserInfo> list = session.selectList("com.golflearn.mapper.AdminMapper.selectByUserPage",map);
			if(list.size() == 0) {
				throw new FindException("게시글이 없습니다.");
			}
			return list;
		}catch (Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	//수강생 리스트 필터
	@Override
	public List<UserInfo> selectByUserType(int currentPage, int cntPerPage, int userType) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			Map<String, Integer> map = new HashMap<>();
			int endRow = currentPage * cntPerPage;
			int startRow = endRow - cntPerPage + 1;
			map.put("startRow", startRow);
			map.put("endRow", endRow);
			map.put("userType", userType);

			List<UserInfo> list = session.selectList("com.golflearn.mapper.AdminMapper.selectByUserType",map);

			if(list.size() == 0) {
				throw new FindException("회원이 없습니다");
			}
			return list;
		}catch (Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	//유저 정보 상세보기
	@Override
	public UserInfo selectUserDetail(String userId) throws FindException{
		SqlSession session = null;
		UserInfo userInfo = null;
		try {
			session = sqlSessionFactory.openSession();
			userInfo = session.selectOne("com.golflearn.mapper.AdminMapper.selectUserDetail", userId);
			if(userInfo == null) {
				throw new FindException("회원이 없습니다");
			}
			return userInfo;
		}catch (Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	//수강생 레슨목록 불러오기
	@Override
	public List<LessonLine> selectByStdtLesson(String userId) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			List<LessonLine> list = session.selectList("com.golflearn.mapper.AdminMapper.selectByStdtLesson",userId);

			if(list.size() == 0) {
				throw new FindException("현재 수강중인 강의가 없습니다");
			}
			return list;
		}catch (Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	//프로 레슨목록 불러오기
	@Override
	public List<Lesson> selectByProLesson(int currentPage, int cntPerPage, String userId) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			Map<String, Object> map = new HashMap<>();
			int endRow = currentPage * cntPerPage;
			int startRow = endRow - cntPerPage + 1;
			map.put("startRow", startRow);
			map.put("endRow", endRow);
			map.put("userType", userId);

			List<Lesson> list = session.selectOne("com.golflearn.mapper.AdminMapper.selectByUserTypeProLesson",map);

			if(list.size() == 0) {
				throw new FindException("현재 개설중인 강의가 없습니다");
			}
			return list;
		}catch (Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}

	}

	//검색어로 유저 찾기
	@Override
	public List<UserInfo> selectByUserSearching(int currentPage, int cntPerPage, String word) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			Map<String, Object> map = new HashMap<>();

			int endRow = currentPage * cntPerPage;
			int startRow = endRow - cntPerPage + 1;

			map.put("startRow", startRow);
			map.put("endRow", endRow);
			map.put("word", word);
			List<UserInfo> list = session.selectList("com.golflearn.mapper.AdminMapper.selectByWord",map);
			if(list.size() == 0) {
				throw new FindException("일치하는 회원이 없습니다");
			}
			return list;
		}catch (Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}
	@Override
	public int selectCountAllUser() throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne("com.golflearn.mapper.AdminMapper.selectCountAllUser");
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	@Override
	public int selectCountUserType(int userType) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne("com.golflearn.mapper.AdminMapper.selectCountUserType");
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	@Override
	public int selectCountStdtLesson(String userId) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne("com.golflearn.mapper.AdminMapper.selectCountStdtLesson");
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	@Override
	public int selectCountProLesson(String userId) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne("com.golflearn.mapper.AdminMapper.selectCountProLesson");
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	@Override
	public List<LessonLine> selectUserLesson(int currentPage, int cntPerPage, int userType, String userId) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			HashMap<String, Object> hashMap = new HashMap<>();
			int endRow = currentPage * cntPerPage;
			int startRow = endRow - cntPerPage + 1;

			hashMap.put("startRow", startRow);
			hashMap.put("endRow", endRow);
			hashMap.put("userType", userType);
			hashMap.put("userId", userId);

			List<LessonLine> list = session.selectList("com.golflearn.mapper.AdminMapper.selectUserLesson",hashMap);

			if(list.size() == 0) {
				throw new FindException("레슨 내역이 없습니다");
			}
			return list;
		}catch (Exception e) {
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	@Override
	public int selectCountSearchUser(String word) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			return session.selectOne("com.golflearn.mapper.AdminMapper.selectCountSearchUser");
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}
}
