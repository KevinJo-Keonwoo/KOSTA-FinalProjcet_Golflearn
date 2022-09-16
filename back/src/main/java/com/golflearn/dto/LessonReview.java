package com.golflearn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class LessonReview {
	private String review;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date reviewDt;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date reviewEditDt;
	private int myStarScore;
	private LessonLine lsnLine;
	private String stdtNickname;//수강생닉네임(DB존재X) -레슨상세보기페이지 서브쿼리구문
  }
