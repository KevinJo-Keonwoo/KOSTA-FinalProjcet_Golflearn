package com.golflearn.dto;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"userId"})

public class UserInfo {
	private String userId;
	private String userName;
	private String userNickname;
	private String userPwd;
	private String userPhone;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date userJoinDt;
	private int userType;
	private int userQuitStatus;
	
// @Nonnull 사용방법 붙이는 경우 질문 
	private ProInfo proInfo;
	
	
}
