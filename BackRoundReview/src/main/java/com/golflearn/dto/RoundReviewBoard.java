package com.golflearn.dto;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"roundReviewBoardNo"})
public class RoundReviewBoard {
private Long roundReviewBoardNo;
	private String roundReviewBoardTitle; 
	private String roundReviewBoardContent;
	private String userNickname;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@ColumnDefault(value = "SYSDATE")
	private Date roundReviewBoardDt;
	private Long roundReviewBoardViewCnt;
	private Long roundReviewBoardLikeCnt;
	private Long roundReviewBoardCmtCnt;
	private String roundReviewBoardLatitude;
	private String roundReviewBoardLongitude;
}
