package com.golflearn.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.golflearn.domain.entity.ResaleCommentEntity;
import com.golflearn.domain.entity.ResaleLikeEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"resaleBoardNo"})
public class ResaleBoardDto {
	private Long resaleBoardNo;
	
	private String userNickname;
	
	@NotEmpty(message="제목은 필수 입력값입니다.")
	private String resaleBoardTitle;

	@NotEmpty(message="내용은 필수 입력값입니다.")
	private String resaleBoardContent;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date resaleBoardDt;
	
	private Integer resaleBoardViewCnt;
	
	private Integer resaleBoardLikeCnt;
	
	private Integer resaleBoardCmtCnt;
	
	private List<ResaleCommentEntity> resaleComment;
	
	private List<ResaleLikeEntity> resaleLike;

}