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
	
	private float lsnStarScore;	//레슨별점 : pjw
	private float proStarScore;	//프로별점 : pjw
	private int lsnStarPoint;
	
//	private List<LessonClsfc> lsnClsfcs;
//	private List<LessonLine> lsnLines;
	
	//살릴것
	//private UserInfo userInfo;
	
	//레슨분류(many) : 레슨(one) 관계이기에 레슨분류에서 레슨을 갖고있어야함 
//	private LessonClassification lessonClassification;
	
	//살릴것
	//private LessonLine lsnLine;
}
