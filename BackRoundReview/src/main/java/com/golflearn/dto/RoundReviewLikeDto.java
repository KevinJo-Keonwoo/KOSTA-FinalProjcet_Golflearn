package com.golflearn.dto;

import com.golflearn.domain.entity.RoundReviewBoardEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of = {"roundReviewLikeNo"})
public class RoundReviewLikeDto {
	public Long roundReviewLikeNo;
	public RoundReviewBoardEntity roundReviewBoard;
	public String userNickname;
}
