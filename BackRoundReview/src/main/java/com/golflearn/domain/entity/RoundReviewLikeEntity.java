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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "round_review_like")
@SequenceGenerator(name = "roundReviewLike_seq_generator",
					sequenceName = "roundReviewLike_seq",
					initialValue = 19,
					allocationSize = 1)

@Getter @Setter
//@DynamicInsert
//@DynamicUpdate
public class RoundReviewLikeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator ="roundReviewLike_seq_generator")
	@Column(name="round_review_like_no")
	public Long roundReviewLikeNo;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="round_review_board_no")
	public RoundReviewBoardEntity roundReviewBoard;
	
	@NonNull
	@Column(name="user_nickname")
	public String userNickname;
	
}
