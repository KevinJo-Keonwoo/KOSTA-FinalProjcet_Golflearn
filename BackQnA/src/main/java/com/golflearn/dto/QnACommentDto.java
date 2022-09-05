package com.golflearn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat(pattern = "yy/MM/dd", timezone ="Asia/Seoul")
	private Date qnaCmtDt;
	private String userNickname;


	public QnACommentDto(Long commentNo, String qnaCmtContent, Date qnaCmtDt, String userNickname) {
		this.commentNo = commentNo;
		this.qnaCmtContent = qnaCmtContent;
		this.qnaCmtDt = qnaCmtDt;
		this.userNickname = userNickname;
	}
	public QnACommentDto(QnACommentEntity cmtentity) {
		this.commentNo =cmtentity.getCommentNo();
		this.qnaCmtContent = cmtentity.getQnaCmtContent();
		this.qnaCmtDt = cmtentity.getQnaCmtDt();
		this.userNickname = cmtentity.getUserNickname();
	}
	
	public QnACommentEntity toEntity() { //QnACommentDto dto) {

		return QnACommentEntity.builder()
				.commentNo(commentNo)
				.qnaCmtContent(qnaCmtContent)
				.qnaCmtDt(qnaCmtDt)
				.userNickname(userNickname)
				.build();
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public void setCommentNo(Long commentNo) {
		this.commentNo = commentNo;
	}


}
