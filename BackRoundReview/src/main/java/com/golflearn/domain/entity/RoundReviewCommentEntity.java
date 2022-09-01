package com.golflearn.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "round_review_comment")
@SequenceGenerator(name = "roundReviewComment_seq_generator",
					sequenceName = "roundReviewComment_seq",
					initialValue = 10,
					allocationSize = 1)

@Getter @Setter
//@DynamicInsert
//@DynamicUpdate
public class RoundReviewCommentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "roundReviewComment_seq_generator")
	@Column(name="round_review_cmt_no")
	private Long roundReviewCmtNo;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="round_review_board_no")
	private RoundReviewBoardEntity roundReviewBoard;
	
	@NonNull
	@Column(name="round_review_cmt_content")
	private String roundReviewCmtContent;
	
	@NonNull
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@ColumnDefault(value = "SYSDATE")
	@Column(name="round_review_cmt_dt")
	private Date roundReviewCmtDt;
	
	@NonNull
	@Column(name="round_review_cmt_parent_no")
	private Long roundReviewCmtParentNo;
	
	@NonNull
	@Column(name="user_nickname")
	private String userNickname; 
}
