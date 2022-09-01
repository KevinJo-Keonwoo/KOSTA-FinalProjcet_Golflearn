package com.golflearn.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter 
@Entity
@Table(name = "resale_comment")
@SequenceGenerator(name="resale_cmt_generator",
					sequenceName="resale_comment_no_seq",
					initialValue=25,
					allocationSize=1)
@DynamicInsert
@DynamicUpdate
public class ResaleCommentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "resale_cmt_generator")
	@Column(name="resale_cmt_no")
	private Long resaleCmtNo;
//	Long resaleBoardNo;
	
	@Column(name="resale_cmt_content")
	@NotBlank(message="내용은 필수 입력값입니다.")
	private String resaleCmtContent;
	
	@Column(name="resale_cmt_dt")
	@ColumnDefault(value="sysdate")
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date resaleCmtDt;
	
	@Column(name="resale_cmt_parent_no")
	@ColumnDefault(value="0")
	private Long resaleCmtParentNo;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@ManyToOne
	@JoinColumn(name = "resale_board_no")
	private ResaleBoardEntity resaleBoard;
	
}
