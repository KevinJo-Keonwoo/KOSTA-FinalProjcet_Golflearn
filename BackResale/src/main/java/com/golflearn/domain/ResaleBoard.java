package com.golflearn.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"resaleBoardNo"})
public class ResaleBoard {
	private Long resaleBoardNo;
	private String userNickname;
	private String resaleBoardTitle;
	private String reslaeBoardContent;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date resaleBoardDt;
	
	private Long resaleBoardViewCnt;
	private Long resaleBoardLikeCnt;
	private Long resaleBoardCmtCnt;
	
	
}
