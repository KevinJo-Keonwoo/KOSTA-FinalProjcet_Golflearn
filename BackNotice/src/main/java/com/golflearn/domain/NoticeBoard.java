package com.golflearn.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
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
@EqualsAndHashCode(of = {"noticeNo"})
@Table(name= "notice_board")
@SequenceGenerator(name = "noticeboard_seq_generator",
					sequenceName= "notice_board_seq",
					initialValue = 1,
					allocationSize = 1
					)
@DynamicInsert
@DynamicUpdate
public class NoticeBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "noticeboard_seq_generator")
	@Column(name="notice_no")
	private Long noticeNo;
	
	@Column(name="notice_title")
	private String noticeTitle;
	
	@Column(name="notice_content")
	private String noticContent;
	
	@Column(name="notice_dt")
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@ColumnDefault(value = "SYSDATE")
	private Date noticeDt;
	
	@Column(name="notice_view_cnt")
	@ColumnDefault(value = "0")
	private Long noticeViewCnt;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@Column(name="notice_like_cnt")
	@ColumnDefault(value = "0")
	private Long noticeLikeCnt;
	
	@Column(name="notice_cmt_cnt")
	@ColumnDefault(value = "0")
	private Long noticeCmtCnt;
}
