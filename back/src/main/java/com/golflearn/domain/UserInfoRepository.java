package com.golflearn.domain;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.golflearn.dto.ProInfo;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

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
	void insertPro(UserInfo userInfo, ProInfo proInfo) throws AddException;
	
	
	/**
	 * 아이디 중복확인
	 * @param userId
   */
	UserInfo selectByUserId(String userId) throws FindException;
  
	/**
	 * 아이디찾기
	 * 이름과 해드폰 번호에 해당하는 고객의 id를 검색한다
	 * @param userName
	 * @param userPhone 
	 * @return userId
	 * @throws FindException
	 */
	public UserInfo selectByUserNameAndPhone(String userName, String userPhone) throws FindException;

	/**
	 * 핸드폰 번호 조회하기
	 * 아이디와 핸드폰 번호에 해당하는 고객의 핸드폰 번호를 검색해 인증코드 sms 발송한다
	 * @param userId
	 * @param userPhone 
	 * @return userPhone
	 * @throws FindException
	 */
	
	public UserInfo selectByUserIdAndPhone(String userId, String userPhone) throws FindException;
	/**
	 * 비밀번호 변경
	 * 변경할 비밀번호와 비밀번호 확인
	 * @param userId
	 * @param userPwd
	 * @throws ModifyException
	 */
	public void updateByUserPwd(String userId, String userPwd) throws ModifyException;
	
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
	 
}
