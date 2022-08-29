package com.golflearn.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor 
@Setter @Getter 


@Entity
@Table(name = "qna_board_commment")
//@IdClass(QnACommentId.class)

@DynamicInsert 
@DynamicUpdate
public class QnAComment {
	@Id
	@Column(name = "qna_board_no")
	private Long qnaboardNo;
	
	@Column(name = "qna_cmt_content")
	private String qnaCmtContent;
	
	@Column(name = "qna_cmt_dt")
	private Date qnaCmtDt;
	
	@Column(name = "user_nickname")
	private String userNickname;
}
