package com.golflearn.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"lsnNo"})
public class Lesson {
	private int lsnNo;
	private String locNo;
	private String lsnTitle;
	private String lsnLv;	//고치기
	private int lsnDays;
	private String lsnIntro;
	private int lsnPrice;
	private int lsnPerTime;
	private int lsnCntSum;
	private int lsnStarSum;
	private int lsnStarPplCnt;
	private int lsnStatus;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date lsnReqDt;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date lsnApvDt;
	private String lsnRjtReason;

	private float lsnStarScore;	//레슨별점(DB존재X) -레슨상세보기페이지
	private float proStarScore;	//프로별점(DB존재X) -레슨상세보기페이지
	private String stdtNickname;//수강생닉네임(DB존재X) -레슨상세보기페이지 서브쿼리구문
	private int lsnStarPoint;
  
	private List<LessonClassification> lsnClassifications;
	
	private UserInfo userInfo;

	private List<LessonLine> lsnLines;//하나의 레슨에 여러 레슨내역

	private LessonLine lsnLine;
	private LessonReview lsnReview;
}
