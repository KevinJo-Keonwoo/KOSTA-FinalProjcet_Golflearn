package com.golflearn.domain;

import java.util.HashMap;

import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.golflearn.dto.ProInfo;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@Repository(value="userInfoRepository")
public class UserInfoOracleRepository implements UserInfoRepository {
	//Mybatis 사용하기 위해 Autowired 된 SqlSessionFactory가 필요
	//SqlSessionFactory를 사용하기 위해선 스프링 컨테이너에 의해 관리가 되어야함
	@Autowired
	private SqlSessionFactory sqlSessionFactory; // sqlSessionFactory : sqlSession을 만드는 역할
	
	// 수강생 회원가입
	@Override
	public void insertStdt(UserInfo userInfo) throws AddException {
		SqlSession session = null;
		try {			
		session = sqlSessionFactory.openSession();
		session.insert("com.golflearn.mapper.UserInfoMapper.insertStdt", userInfo);
		} catch(Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}	
	}
	
	// 프로 회원가입
	@Transactional
	@Override
	public void insertPro(UserInfo userInfo, ProInfo proInfo) throws AddException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			session.insert("com.golflearn.mapper.UserInfoMapper.insertPro", userInfo);
			session.insert("com.golflearn.mapper.UserInfoMapper.insertProInfo", proInfo);
		} catch(Exception e) {e.printStackTrace();
			throw new AddException(e.getMessage());
		} finally {
			if(session != null) {
			session.close();
			}
		}				
	}
	
	//아이디 중복확인
	@Override
	public UserInfo selectByUserId(String userId) throws FindException {
		SqlSession session = null; // sqlSession : 실제 sql을 날리는 역할
		try {
			session = sqlSessionFactory.openSession();
			// selectOne (namespace.select태그의 id속성값 , Where절의 id값) -> rs.next() 로 리턴받는것 까지 해줌
	
			//	첫번째 인자의 mapper class 찾아가고 그 id속성으로 들어간 다음
			//	두번째 인자의 id값이 CustomerMapper의 Where절의 #{id}로 감 (전달값)
			UserInfo userInfo = session.selectOne("com.golflearn.mapper.UserInfoMapper.selectByUserId", userId);
			if(userInfo == null) {
				throw new FindException("중복된 아이디가 없습니다.");
			}
			return userInfo;
			// SQL 구문 -> Oracle로 감 -> 결과 받아옴 -> UserInfo 객체 형태로 반환 -> 변수에 넣어 반환
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close(); //Connection pool에다가 돌려줌
			}
		}
	}
	
	//닉네임 중복확인
	@Override
	public UserInfo selectByUserNickName(String userNickname) throws FindException {
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			UserInfo userInfo = session.selectOne("com.golflearn.mapper.UserInfoMapper.selectByUserNickname", userNickname);
			if(userInfo == null) {
				throw new FindException("중복된 닉네임이 없습니다");
			}
			return userInfo;
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	// 로그인
	@Override
	public UserInfo selectByUserIdAndPwd(String userId, String userPwd) throws FindException {
		UserInfo userInfo = null;
		SqlSession session = null;
		
		try {
			session = sqlSessionFactory.openSession();
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("userId", userId);
			hashMap.put("userPwd", userPwd);
			userInfo = session.selectOne("com.golflearn.mapper.UserInfoMapper.selectByUserIdAndPwd",hashMap);
			
			return userInfo;
		} catch(Exception e){
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	//아이디 찾기
	@Override
	public UserInfo selectByUserNameAndPhone(String userName, String userPhone) throws FindException {
		UserInfo userInfo = null; 
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();

			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("userName", userName);
			hashMap.put("userPhone", userPhone);
			userInfo = session.selectOne("com.golflearn.mapper.UserInfoMapper.selectByUserNameAndPhone", hashMap);

			if (userInfo == null) {
				throw new FindException("Id조회실패");
			}
			return userInfo;
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	//핸드폰번호 조회
	@Override
	public UserInfo selectByUserIdAndPhone(String userId, String userPhone) throws FindException {
		UserInfo userInfo = null;
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();

			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("userId", userId);
			hashMap.put("userPhone", userPhone);
			userInfo = session.selectOne("com.golflearn.mapper.UserInfoMapper.selectByUserIdAndPhone",hashMap);

			if (userInfo == null) {
				throw new FindException("정보조회 실패");		
			}
			return userInfo;
		}catch(Exception e) {
			e.printStackTrace();
			throw new FindException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	//비밀번호 변경
	@Override
	public void updateByUserPwd(String userId, String userPwd) throws ModifyException{
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("userId", userId);
			hashMap.put("userPwd", userPwd);
			session.update("com.golflearn.mapper.UserInfoMapper.updateByUserPwd",hashMap);
		}catch(Exception e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
}