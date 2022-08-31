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
public class ResaleLike {
	private Long resaleLikeNo;
//	private Long resaleBoardNo;
	private ResaleBoard resaleBoard;
	private String userNickname;
}
