package com.golflearn.dto;


import com.golflearn.domain.entity.NoticeLikeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class NoticeLikeDto {
	private Long noticeLikeNo;
	private NoticeBoardDto noticeBoardDto;
//	private Long noticeBoardNo;
	private String userNickname;
	
	private NoticeLikeDto (Long noticeLikeNo, NoticeBoardDto noticeBoardDto, String userNickname) {
		this.noticeLikeNo = noticeLikeNo;
		this.noticeBoardDto = noticeBoardDto;
//		this.noticeBoardNo = noticeBoardNo;
		this.userNickname = userNickname;
	}
	
//	public NoticeLikeEntity toEntity() {
//		return NoticeLikeEntity.builder()
//				.noticeLikeNo(noticeLikeNo)
//				.noticeBoard(noticeBoardDto.toEntity())
//				.userNickname(userNickname)
//				.build();
//	}
}
