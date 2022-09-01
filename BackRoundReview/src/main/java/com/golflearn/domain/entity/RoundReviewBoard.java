package com.golflearn.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.NonNull;

@Entity
@Table(name = "round_review_board")
@SequenceGenerator(name = "roundReviewBoard_seq_generator",
					sequenceName = "roundReviewBoard_seq",
					initialValue = 26,
					allocationSize = 1)
//@DynamicInsert
//@DynamicUpdate			
public class RoundReviewBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "roundReviewBoard_seq_generator")
	@Column(name="round_review_board_no")
	private Long roundReviewBoardNo;
	
	@NonNull
	@Column(name="round_review_board_title")
	private String roundReviewBoardTitle; 
	
	@NonNull
	@Column(name="round_review_board_content")
	private String roundReviewBoardContent;
	
	@NonNull
	@Column(name="user_nickname")
	private String userNickname;
	
	@NonNull
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@ColumnDefault(value = "SYSDATE")
	@Column(name="round_review_board_dt")
	private Date roundReviewBoardDt;
	
	@Column(name="round_review_board_view_cnt")
	private Long roundReviewBoardViewCnt;
	
	@Column(name="round_review_board_like_cnt")
	private Long roundReviewBoardLikeCnt;
	
	@Column(name="round_review_board_cmt_cnt")
	private Long roundReviewBoardCmtCnt;
	
	@Column(name="round_review_board_latitude")
	private String roundReviewBoardLatitude;
	
	@Column(name="round_review_board_longitude")
	private String roundReviewBoardLongitude;
	
}
