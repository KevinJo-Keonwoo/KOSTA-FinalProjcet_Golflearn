package com.golflearn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golflearn.domain.entity.QnABoardEntity;
import com.golflearn.domain.entity.QnACommentEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QnABoardDto {
	private Long boardNo;
	private String boardTitle;
	private String userNickname;
	private String boardContent;
	@JsonFormat(pattern = "yy/MM/dd", timezone ="Asia/Seoul")
	private Date qnaBoardDt;
	private Integer qnaBoardSecret;
	private QnACommentEntity comment;
			
	
	public QnABoardDto(Long boardNo, String boardTitle, String userNickname, String boardContent, Date qnaBoardDt,
			int qnaBoardSecret, QnACommentEntity comment) {
		this.boardNo = boardNo;
		this.boardTitle = boardTitle;
		this.userNickname = userNickname;
		this.boardContent = boardContent;
		this.qnaBoardDt = qnaBoardDt;
		this.qnaBoardSecret = qnaBoardSecret;
		this.comment = comment;
	}
	
	public QnABoardDto(QnABoardEntity boardentity) {
		this.boardNo = boardentity.getBoardNo();
		this.boardTitle = boardentity.getBoardTitle();
		this.userNickname = boardentity.getUserNickname();
		this.boardContent = boardentity.getBoardContent();
		this.qnaBoardDt = boardentity.getQnaBoardDt();
		this.qnaBoardSecret = boardentity.getQnaBoardSecret();
		this.comment = boardentity.getComment();
	}
	
	public QnABoardEntity toEntity() {
		return QnABoardEntity.builder()
				.boardNo(boardNo)
				.boardTitle(boardTitle)
				.userNickname(userNickname)
				.boardContent(boardContent)
				.qnaBoardDt(qnaBoardDt)
				.qnaBoardSecret(qnaBoardSecret)
				.comment(comment)
				.build();
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public void setBoardNo(Long boardNo) {
		this.boardNo = boardNo;
	}

	public void setComment(QnACommentEntity comment) {
		this.comment = comment;
	}
}

	
