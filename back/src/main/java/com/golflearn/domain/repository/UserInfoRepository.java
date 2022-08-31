package com.golflearn.domain.repository;

import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

public interface UserInfoRepository {
/*
 * 1) 수강생 회원가입
 * 2) 프로 회원가입
 * 3) 아이디 중복확인
 * 4) 로그인
 * 5) 아이디 찾기
 * 6) 비밀번호 찾기
 * 7) 비밀번호 변경
 */
	
	/**
	 * 수강생 회원 정보를 추가한다	 * 
	 * @throws AddException
	 */
	void insertStdt() throws AddException;
	
	/**
	 * 프로 회원 정보를 추가한다
	 * 
	 */
	void insertPro() throws AddException ;
	
	
	/**
	 * 아이디 중복확인
	 * @param userId
	 * @return
	 */
	void selectByuserId() throws FindException;
	
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
	 * 비밀번호 찾기
	 * 아이디와 핸드폰 번호에 해당하는 고객의 핸드폰 번호를 검색한다
	 * @param userId
	 * @param userPhone 
	 * @return userPhone
	 * @throws FindException
	 */
	
	public UserInfo selectByUserIdAndPhone(String userId, String userPhone) throws FindException;
	
	/**
	 * 비밀번호 변경
	 * 변경할 비밀번호와 비밀번호 확인
	 * @param
	 * @param
	 * @throws ModifyException
	 */
	public void updateByUserPwd(String userId, String userPwd) throws ModifyException;
}
