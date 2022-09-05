package com.golflearn.dto;

import java.util.Date;

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
	private String resaleBoardTitle;
	private String reslaeBoardContent;
	

	private Date resaleBoardDt;
	
	private Long resaleBoardViewCnt;
	private Long resaleBoardLikeCnt;
	private Long resaleBoardCmtCnt;
	
	
}
