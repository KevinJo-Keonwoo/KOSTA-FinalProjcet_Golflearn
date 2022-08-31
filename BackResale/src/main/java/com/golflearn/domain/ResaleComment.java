package com.golflearn.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
@Table(name = "resale_comment")
public class ResaleComment {
	@Id
	@Column(name="resale_cmt_no")
	private Long resaleCmtNo;
//	Long resaleBoardNo;
	
	private ResaleBoard resaleBoard;
	
	@Column(name="resale_cmt_content")
	private String resaleCmtContent;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@Column(name="resale_cmt_dt")
	@ColumnDefault(value="SYSDATE")
	private Date resaleCmtDt;
	
	@Column(name="resale_cmt_parent_no")
	@ColumnDefault(value="0")
	private Long resaleCmtParentNo;
	
//	private String userNickname;
	
}
