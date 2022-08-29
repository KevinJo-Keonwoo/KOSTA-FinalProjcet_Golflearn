package com.golflearn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golflearn.domain.repository.UserInfoRepository;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;

@Service
public class UserInfoService {
	
	@Autowired // 빈 객체 주입받음
	private UserInfoRepository repository;
	
	// 회원가입 - 수강생
	public void signupStdt() throws AddException {
		
	}
	
	// 회원가입 - 프로
	
	// 아이디 중복확인
	
	// 닉네임 중복확인
	
	// 로그인
	public UserInfo login(String userId, String userPwd) throws FindException {
		return null;
	}
}
