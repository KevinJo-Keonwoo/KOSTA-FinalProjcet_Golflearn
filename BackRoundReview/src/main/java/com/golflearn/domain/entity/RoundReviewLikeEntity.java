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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "round_review_like")
@SequenceGenerator(name = "roundReviewLike_seq_generator",
					sequenceName = "roundReviewLike_seq",
					initialValue = 19,
					allocationSize = 1)

@Getter @Setter
@DynamicInsert
@DynamicUpdate
public class RoundReviewLikeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator ="roundReviewLike_seq_generator")
	@Column(name="round_review_like_no")
	public Long roundReviewLikeNo;
	
	//양방향 연관관계 
	@JsonBackReference//연관관계의 주인 Entity 에 선언. 직렬화 되지 않도록 수행
	@ManyToOne
	@JoinColumn(name="round_review_board_no")
	public RoundReviewBoardEntity roundReviewBoard;
	
//	@Column(name="round_review_board_no")
//	public Long roundReviewBoardNo;

	
	@Column(name="user_nickname")
	public String userNickname;
	
}
