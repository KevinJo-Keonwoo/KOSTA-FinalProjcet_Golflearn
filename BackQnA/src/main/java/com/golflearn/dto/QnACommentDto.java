package com.golflearn.dto;

import java.util.Date;

import com.golflearn.domain.entity.QnACommentEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QnACommentDto {
	private Long commentNo;
	private String qnaCmtContent;
	private Date qnaCmtDt;
	private String userNickname;


	public QnACommentDto(Long commentNo, String qnaCmtContent, Date qnaCmtDt, String userNickname) {
		this.commentNo = commentNo;
		this.qnaCmtContent = qnaCmtContent;
		this.qnaCmtDt = qnaCmtDt;
		this.userNickname = userNickname;
	}
	public QnACommentEntity toEntity() {
		return QnACommentEntity.builder()
				.commentNo(commentNo)
				.qnaCmtContent(qnaCmtContent)
				.qnaCmtDt(qnaCmtDt)
				.userNickname(userNickname)
				.build();
	}


}
