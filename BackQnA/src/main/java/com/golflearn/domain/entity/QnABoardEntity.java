package com.golflearn.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor 
@Setter @Getter 
@EqualsAndHashCode(of= {"boardNo"})
@Builder
@Entity
@Table(name = "qna_board")
@SequenceGenerator(name = "qna_board_no_seq_generator",
					sequenceName = "qna_board_no_seq",
					initialValue = 1,
					allocationSize = 1
					)

//@SecondaryTable(name="qna_board_commment", pkJoinColumns = @PrimaryKeyJoinColumn(name="qna_board_no", referencedColumnName = "qna_board_no"))

@DynamicInsert  
@DynamicUpdate
public class QnABoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
		 	generator = "qna_board_no_seq_generator")
	@Column(name = "qna_board_no")
	private Long boardNo;
//	@ColumnDefault          
	@Column(name = "qna_board_title")
	private String boardTitle;
	
	@Column(name = "qna_board_content")
	private String boardContent;
	
	@Column(name = "user_nickname")
	private String userNickname;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone ="Asia/Seoul")
	@Column(name = "qna_board_dt")                                             
	@ColumnDefault(value = "SYSDATE")
	private Date qnaBoardDt;
	
	@Column(name = "qna_board_secret")
	private Integer qnaBoardSecret;
	
	
	//1. 부모쪽 1:N	
//	@OneToMany
//	@JoinColumn(name = "qna_comment_no")
//	private List<QnAComment> list;

	//2. 부모쪽 1:1
	@OneToOne (mappedBy = "board")
	private QnACommentEntity comment;
}
