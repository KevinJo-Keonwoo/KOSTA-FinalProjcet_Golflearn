package com.golflearn.domain.repository;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@Repository
public class UserInfoOracleRepository implements UserInfoRepository {
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Override
	public void insertStdt() throws AddException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertPro() throws AddException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectByuserId() throws FindException {
		// TODO Auto-generated method stub
		
	}

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
