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
@EqualsAndHashCode(of = {"lsnLineNo"})
public class LessonReview {
	private String review;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date reviewDt;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date reviewEditDt;
	private int myStarScore;
	private LessonLine lsnLine;
  }
