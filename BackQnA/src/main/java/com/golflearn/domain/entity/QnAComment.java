package com.golflearn.domain.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
@Table(name = "qna_commment")
//@IdClass(QnACommentId.class)

@DynamicInsert 
@DynamicUpdate
public class QnAComment {
	@Id
	@Column(name = "qna_comment_no")
	private Long cNo;
	
	@Column(name = "qna_cmt_content")	
	private String qnaCmtContent;
	
	@Column(name = "qna_cmt_dt")		
	private Date qnaCmtDt;
	
	@Column(name = "user_nickname")
	private String userNickname;
//1. 자식쪽 없음
	
	
//2. 자식쪽 1:1	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "qna_comment_no")//insertable = false, updatable = false, nullable = true)
	@MapsId(value =  "bNo")
	private QnABoard board;
}
