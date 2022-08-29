package com.golflearn.domain.repository;

import com.golflearn.dto.ProInfo;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;

public interface UserInfoRepository {
/*
 * 1) 수강생 회원가입
 * 2) 프로 회원가입
 * 3) 아이디 중복확인
 * 4) 닉네임 중복확인
 * 5) 로그인
 * 6) 아이디 찾기
 * 7) 비밀번호 찾기
 * 
 */
	
	/**
	 * 수강생 회원 정보를 추가한다	 * 
	 * @throws AddException
	 */
	void insertStdt(UserInfo userInfo) throws AddException;
	
	/**
	 * 프로 회원 정보를 추가한다
	 * @throws AddException
	 */
	void insertPro(UserInfo userInfo, ProInfo proInfo) throws AddException ;
	
	
	/**
	 * 아이디 중복확인
	 * @param userId
	 */
	UserInfo selectByuserId(String userId) throws FindException;
	
	/**
	 * 닉네임 중복확인
	 * @throws FindException
	 */
	UserInfo selectByUserNickName(String userNickname) throws FindException;
	
	/**
	 * 로그인
	 * @param userId
	 * @param userPwd
	 * @throws FindException
	 */
	UserInfo selectByUserIdAndPwd(String userId, String userPwd) throws FindException;
	 
	/**
	 * 아이디찾기
	 * @throws FindException
	 */
	void selectById() throws FindException;
	
	
	/**
	 * 비밀번호 찾기
	 * @throws FindException
	 */
	
	void selectByUserPwd() throws FindException;
}
