package com.golflearn.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "resale_board")
@SequenceGenerator(name ="resale_board_generator",
					sequenceName="resale_board_no_seq",
					initialValue=28, // 28부터 시작(샘플데이터가 27까지 있음)
					allocationSize=1 // 1씩 증가
					)
public class ResaleBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "resale_board_generator")
	@Column(name="resale_board_no")
	private Long resaleBoardNo;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@Column(name="resale_board_title")
	private String resaleBoardTitle;
	
	@Column(name="resale_board_content")
	private String resaleBoardContent;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@Column(name="resale_board_dt")
	@ColumnDefault(value="SYSDATE")
	private Date resaleBoardDt;
	
	@Column(name="resale_board_view_cnt")
	@ColumnDefault(value="0")
	private Long resaleBoardViewCnt;
	
	@Column(name="resale_board_like_cnt")
	@ColumnDefault(value="0")
	private Long resaleBoardLikeCnt;
	
	@Column(name="resale_board_cmt_cnt")
	@ColumnDefault(value="0")
	private Long resaleBoardCmtCnt;
	
	@OneToMany
	private List<ResaleComment> resaleComment;
	
}
