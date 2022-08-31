package com.golflearn.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"resaleBoardNo"})
public class ResaleBoard {
	private Long resaleBoardNo;
	private String userNickname;
	
	@NotEmpty(message="제목은 필수 입력값입니다.")
	private String resaleBoardTitle;
	@NotEmpty(message="내용은 필수 입력값입니다.")
	private String reslaeBoardContent;
	

	private Date resaleBoardDt;
	
	private Long resaleBoardViewCnt;
	private Long resaleBoardLikeCnt;
	private Long resaleBoardCmtCnt;
	
	
}
