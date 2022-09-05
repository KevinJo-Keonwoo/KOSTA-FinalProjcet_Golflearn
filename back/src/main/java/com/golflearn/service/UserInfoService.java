package com.golflearn.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.golflearn.domain.UserInfoRepository;
import com.golflearn.dto.ProInfo;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;

@Service
public class UserInfoService {
	
	@Autowired // 빈 객체 주입받음
	private UserInfoRepository repository;
	
	//아이디 찾기
	public UserInfo selectByUserNameAndPhone(String userName, String userPhone) throws FindException{
		UserInfo userInfo = repository.selectByUserNameAndPhone(userName, userPhone);
		return userInfo;
	}
	
	//핸드폰번호조회
	public UserInfo selectByUserIdAndPhone(String userId, String userPhone) throws FindException, JsonProcessingException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException{
		UserInfo userInfo = repository.selectByUserIdAndPhone(userId, userPhone);	
		return userInfo;
	}
	
	//비밀번호 변경
	public void updateByUserPwd(String userId, String userPwd) throws ModifyException{
		repository.updateByUserPwd(userId, userPwd);
	}
	
	// 회원가입 - 수강생
	public void signupStdt(UserInfo userInfo) throws AddException {
		repository.insertStdt(userInfo);
	}
	
	// 회원가입 - 프로
	public void signuppro(UserInfo userInfo, ProInfo proInfo) throws AddException{
		repository.insertPro(userInfo, proInfo);
	}
	
	// 아이디 중복확인
	public UserInfo iddupchk(String userId) throws FindException {
		return repository.selectByUserId(userId);
	}	
	
	// 닉네임 중복확인
	public UserInfo nicknamedupchk(String userNickname)throws FindException {
		return repository.selectByUserNickName(userNickname);
	}
	
	// 로그인
	public UserInfo login(String userId, String userPwd) throws FindException {
		UserInfo userInfo = repository.selectByUserIdAndPwd(userId, userPwd);
		if(!userInfo.getUserPwd().equals(userPwd)) {
			throw new FindException();
		}
		return userInfo;
	}
	
}
