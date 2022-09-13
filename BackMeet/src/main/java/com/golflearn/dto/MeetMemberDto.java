package com.golflearn.dto;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@DynamicInsert
@DynamicUpdate
public class MeetMemberDto {

	private MeetBoardDto meetBoard;

	private Long meetMbNo;

	private String userNickname;

}
