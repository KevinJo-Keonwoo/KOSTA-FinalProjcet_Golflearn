package com.golflearn.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@EqualsAndHashCode(of = {"noticeCmtNo"})
@Table(name= "notice_comment")
@SequenceGenerator(name = "noticecomment_seq_generator",
					sequenceName= "notice_comment_seq",
					initialValue = 1,
					allocationSize = 1
					)
@DynamicInsert
@DynamicUpdate
public class NoticeComment {
	@Id
	@GeneratedValue 
	@ColumnDefault(value = "0")
	private Long noticeCmtNo;
	
	@ManyToOne
	@JoinColumn(name="notice_board_no")
	private NoticeBoard noticeBoard;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@Column(name="user_cmt_content")
	private String noticeCmtContent;
	
	@Column(name="notice_dt")
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@ColumnDefault(value = "SYSDATE")
	private Date noticeCmtDt;
	
	@Column(name="notice_cmt_parent_no")
	@ColumnDefault(value = "-1")
	private Long noticeCmtParentNo;
}
