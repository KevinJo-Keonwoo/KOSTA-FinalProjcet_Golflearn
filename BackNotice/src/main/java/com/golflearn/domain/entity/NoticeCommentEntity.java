package com.golflearn.domain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.golflearn.dto.NoticeBoardDto;
import com.golflearn.dto.NoticeCommentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"noticeCmtNo"})
@Table(name= "notice_comment")
@SequenceGenerator(name = "noticecomment_seq_generator",
sequenceName= "notice_comment_no_seq",
initialValue = 1,
allocationSize = 1
		)
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class NoticeCommentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "noticecomment_seq_generator")
	@ColumnDefault(value = "0")
	@Column(name="notice_cmt_no")
	private Long noticeCmtNo;

	@JsonBackReference
	@ManyToOne(optional = false)
	@JoinColumn(name="notice_board_no")
	private NoticeBoardEntity noticeBoard;

	@Column(name="user_nickname")
	private String userNickname;

	@Column(name="notice_cmt_content")
	private String noticeCmtContent;

	@Column(name="notice_cmt_dt")
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@ColumnDefault(value = "SYSDATE")
	private Date noticeCmtDt;

	@Column(name="notice_cmt_parent_no")
	@ColumnDefault(value = "-1")
	private Long noticeCmtParentNo;
	
	public NoticeCommentDto toDto() {
//		noticeBoard = NoticeBoardEntity.builder()
//								.noticeBoardNo(noticeBoard.getNoticeBoardNo())
//								.build();
//		System.out.println("--------" + noticeBoard.getNoticeBoardNo());
//		NoticeBoardDto noticeBoardDto = noticeBoard.toDto();
		return NoticeCommentDto.builder()
				.noticeCmtNo(noticeCmtNo)
				.noticeCmtDt(noticeCmtDt)
				.noticeBoardNo(noticeBoard.getNoticeBoardNo())
				.noticeCmtContent(noticeCmtContent)
				.noticeCmtParentNo(noticeCmtParentNo)
				.userNickname(userNickname)
				.build();
	}
}
