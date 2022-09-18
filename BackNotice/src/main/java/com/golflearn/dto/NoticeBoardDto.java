package com.golflearn.dto;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golflearn.domain.entity.NoticeBoardEntity;
import com.golflearn.domain.entity.NoticeCommentEntity;
import com.golflearn.domain.entity.NoticeLikeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
//@DynamicInsert
//@DynamicUpdate
public class NoticeBoardDto {
	private Long noticeBoardNo;
	private String noticeBoardTitle;
	private String noticeBoardContent;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date noticeBoardDt;
	private Long noticeBoardViewCnt;
	private Long noticeBoardLikeCnt;
	private Long noticeBoardCmtCnt;
	private String userNickname;
	@Singular("cmtList")
	private List<NoticeCommentEntity> noticeCommentList;
	private List<NoticeLikeEntity> noticeLikeList;
	
	@Builder
	public NoticeBoardDto(Long noticeBoardNo, String noticeBoardTitle, String noticeBoardContent, Date noticeBoardDt, Long noticeBoardViewCnt, Long noticeBoardLikeCnt, Long noticeBoardCmtCnt, String userNickname, List<NoticeCommentEntity> noticeCommentList, List<NoticeLikeEntity> noticeLikeList) {
		this.noticeBoardNo = noticeBoardNo;
		this.noticeBoardTitle = noticeBoardTitle;
		this.noticeBoardContent = noticeBoardContent;
		this.noticeBoardDt = noticeBoardDt;
		this.noticeBoardViewCnt = noticeBoardViewCnt;
		this.noticeBoardLikeCnt = noticeBoardLikeCnt;
		this.noticeBoardCmtCnt = noticeBoardCmtCnt;
		this.userNickname = userNickname;
		this.noticeCommentList = noticeCommentList;
		this.noticeLikeList = noticeLikeList;
	}

	@Builder
	public NoticeBoardDto (Long noticeBoardNo, String noticeBoardTitle, String noticeBoardContent, String userNickname){
		this.noticeBoardNo = noticeBoardNo;
		this.noticeBoardTitle = noticeBoardTitle;
		this.noticeBoardContent = noticeBoardContent;
		this.userNickname = userNickname;
	}
	
//	@Builder
//	public NoticeBoardDto (String noticeBoardTitle, String noticeBoardContent){
//		this.noticeBoardTitle = noticeBoardTitle;
//		this.noticeBoardContent = noticeBoardContent;
//
//	}
	
	public NoticeBoardEntity toEntity() {
		return NoticeBoardEntity.builder()
				.noticeBoardNo(noticeBoardNo)
				.noticeBoardTitle(noticeBoardTitle)
				.noticeBoardContent(noticeBoardContent)
				.noticeBoardCmtCnt(noticeBoardCmtCnt)
				.noticeBoardDt(noticeBoardDt)
				.noticeBoardLikeCnt(noticeBoardLikeCnt)
				.noticeBoardViewCnt(noticeBoardViewCnt)
				.userNickname(userNickname)
//				.noticeCommentList(noticeCommentList)
				.build();
	}
}

