package com.golflearn.dto;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@DynamicInsert
@DynamicUpdate
public class MeetBoardDto {
	private MeetCategoryDto meetCategory;
	
	private Long meetBoardNo;

	private String userNickname;

	private String meetBoardTitle;

	private String meetBoardContent;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date meetBoardDt;

	private Long meetBoardStatus;

	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul") 
	private Date meetBoardMeetDt;

	private Long meetBaordCurCnt;

	private Long meetBoardMaxCnt;

	private String meetBoardLocation;
	
	private Long meetBoardViewCnt;


}
