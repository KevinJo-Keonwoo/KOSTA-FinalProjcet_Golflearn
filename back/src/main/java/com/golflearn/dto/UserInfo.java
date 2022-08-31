package com.golflearn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"userId"})
@Data
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
//	private Pro pro;
	
	
}
