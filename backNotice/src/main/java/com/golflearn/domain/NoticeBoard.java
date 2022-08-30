package com.golflearn.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"noticeBoardNo"})
@DynamicInsert
@DynamicUpdate
public class NoticeBoard {
	@Id
	@GeneratedValue
	@Column
	private Long noticeBoardNo;
	
	@Column
	private String noticeBoardTitle;
	
	@Column
	private String noticeBoardContent;
	
	@Column
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date noticeBoardDt;
	
	@Column
	private Long noticeBoardViewCnt;
	
	@Column
	private String userNickname;
	
	@Column
	private Long noticeBoardLikeCnt;
	
	@Column
	private Long noticeBoardCmtCnt;
}
