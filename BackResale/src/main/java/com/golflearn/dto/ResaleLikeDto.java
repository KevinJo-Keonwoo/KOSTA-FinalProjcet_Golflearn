package com.golflearn.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"resaleLikeNo"})
//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResaleLikeDto {
	private Long resaleLikeNo;
	private String userNickname;
	private ResaleBoardDto resaleBoard;
}
