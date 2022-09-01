package com.golflearn.dto;

import com.golflearn.domain.entity.RoundReviewBoard;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"roundReviewLikeNo"})
public class RoundReviewLike {
	public Long roundReviewLikeNo;
	public RoundReviewBoard roundReviewBoard;
	public String userNickname;
}
