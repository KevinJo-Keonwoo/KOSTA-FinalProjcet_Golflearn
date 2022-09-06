package com.golflearn.dto;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golflearn.domain.entity.RoundReviewCommentEntity;
import com.golflearn.domain.entity.RoundReviewLikeEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"roundReviewBoardNo"})
public class RoundReviewBoardDto {
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
	private List<RoundReviewCommentEntity> roundReviewCommentList;
	private List<RoundReviewLikeEntity> roundReviewLikeList;
}
