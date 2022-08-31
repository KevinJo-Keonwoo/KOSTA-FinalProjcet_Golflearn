package com.golflearn.dto;

import java.util.Date;

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
@EqualsAndHashCode(of = {"lsnLine"})//lessonLine의 lsnLineNo가 pkey
public class LessonReview {
	private String review;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date reviewDt;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date reviewEditDt;
	private int myStarScore;
	private LessonLine lsnLine;//이거 lessonLine으로 함? 아님 line으로 함? => 통일할 것
}
