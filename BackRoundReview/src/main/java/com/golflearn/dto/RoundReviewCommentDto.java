package com.golflearn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golflearn.domain.entity.RoundReviewBoardEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"roundReviewCmtNo"})
public class RoundReviewCommentDto {
private Long roundReviewCmtNo;
	private RoundReviewBoardEntity roundReviewBoard;
	private String roundReviewCmtContent;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date roundReviewCmtDt;
	private Long roundReviewCmtParentNo;
	private String userNickname; 
}
