package com.golflearn.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.NonNull;

@Entity
@Table(name = "round_review_like")
@SequenceGenerator(name = "roundReviewLike_seq_generator",
					sequenceName = "roundReviewLike_seq",
					initialValue = 19,
					allocationSize = 1)
//@DynamicInsert
//@DynamicUpdate
public class RoundReviewLike {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator ="roundReviewLike_seq_generator")
	@Column(name="round_review_like_no")
	public Long roundReviewLikeNo;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="round_review_board_no")
	public RoundReviewBoard roundReviewBoard;
	
	@NonNull
	@Column(name="user_nickname")
	public String userNickname;
	
}
