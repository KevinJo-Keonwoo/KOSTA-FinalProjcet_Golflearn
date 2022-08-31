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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"roundReviewBoardNo"})

@Entity
@Table(name = "round_review_board_test")
@SequenceGenerator(name = "roundReviewBoard_seq_generator",
					sequenceName = "roundReviewBoard_seq",
					initialValue = 1,
					allocationSize = 1)
//@DynamicInsert
//@DynamicUpdate			
public class RoundReviewBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="round_review_board_no")
	private Long roundReviewBoardNo;
	
	@Column(name="round_review_board_title")
	private String roundReviewBoardTitle; 
	
	@Column(name="round_review_board_content")
	private String roundReviewBoardContent;
	
	@Column(name="user_nickname")
	private String userNickname;
	
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
	
	@Column(name="round_review_board_latitiude")
	private String roundReviewBoardLatitude;
	
	@Column(name="round_review_board_longitude")
	private String roundReviewBoardLongitude;
	
}
