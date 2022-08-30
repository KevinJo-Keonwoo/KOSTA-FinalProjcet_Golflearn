package com.golflearn.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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
	@NotEmpty(message="아이디는 필수 입력값입니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", 
			 message = "이메일 형식에 맞지 않습니다.")
	private String userId;
	
	@NotEmpty(message="이름은 필수 입력값입니다.")
	@Pattern(regexp ="^[A-Za-z가-힣]{2,7}$")
	private String userName;
	
	@NotEmpty(message="닉네임은 필수 입력값입니다.")
	@Pattern(regexp ="^[A-Za-z가-힣0-9]{2,7}$" ,message="닉네임은 영문 대소문자, 숫자, 한글을 사용하세요.")
	private String userNickname;
	
	@NotEmpty(message = "비밀번호는 필수 입력값입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{4,10}$"
	    		, message = "비밀번호는 4~10자 영문 대소문자, 숫자, 특수문자를 사용하세요.")
	private String userPwd;
	
	@NotEmpty(message = "휴대폰 번호는 필수 입력값입니다.")
	@Pattern(regexp = "^[0-9]{3}+-[0-9]{4}+-[0-9]{4}$", 
	 message = "휴대폰번호 형식에 맞지 않습니다.")
	private String userPhone;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date userJoinDt;
	private int userType;
	private int userQuitStatus;
	
// @Nonnull 사용방법 붙이는 경우 질문 
	private ProInfo proInfo;
	
	
}
