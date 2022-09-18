package com.golflearn.domain.entity;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "round_review_board")
@SequenceGenerator(name = "roundReviewBoard_seq_generator",
					sequenceName = "roundReviewBoard_seq",
					initialValue = 26,
					allocationSize = 1)
@Getter @Setter
@DynamicInsert
@DynamicUpdate			
public class RoundReviewBoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "roundReviewBoard_seq_generator")
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
	
	@Column(name="round_review_board_latitude")
	private String roundReviewBoardLatitude;
	
	@Column(name="round_review_board_longitude")
	private String roundReviewBoardLongitude;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "roundReviewBoard", fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
//	@JoinColumn(name="round_review_board_no")
	private List<RoundReviewCommentEntity> roundReviewCommentList;
	
	@JsonManagedReference
//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "RoundReviewBoardEntity")
	@OneToMany(mappedBy = "roundReviewBoard",fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
//	@JoinColumn(name="round_review_board_no")
	private List<RoundReviewLikeEntity> roundReviewLikeList;
	


}
