package com.golflearn.dto;

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
	public RoundReviewBoardDto roundReviewBoard;
	public String userNickname;
}
