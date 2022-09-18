package com.golflearn.domain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.golflearn.dto.NoticeBoardDto;
import com.golflearn.dto.NoticeBoardDto.NoticeBoardDtoBuilder;
import com.golflearn.dto.NoticeCommentDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Entity
@NoArgsConstructor
//(access = AccessLevel.PROTECTED)//기본 생성자(NoArgsConstructor)의 접근 제어를 PROCTECTED 로 설정하면 아무런 값도 갖지 않는 의미 없는 객체의 생성을 막게 됩니다. 즉 무분별한 객체 생성에 대해 한번 더 체크할 수 있습니다.
@AllArgsConstructor
@EqualsAndHashCode(of = {"noticeBoardNo"})
@Table(name= "notice_board")
@SequenceGenerator(name = "noticeboard_seq_generator",
					sequenceName= "notice_board_no_seq",
					initialValue = 1,
					allocationSize = 1
					)
@DynamicInsert
@DynamicUpdate
@Getter @Setter
//@Builder
public class NoticeBoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "noticeboard_seq_generator")
	@Column(name="notice_board_no")
	private Long noticeBoardNo;
	
	@Column(name="notice_board_title")
	private String noticeBoardTitle;
	
	@Column(name="notice_board_content")
	private String noticeBoardContent;
	
	@Column(name="notice_board_dt") 
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@ColumnDefault(value = "SYSDATE")
	private Date noticeBoardDt;
	
	@Column(name="notice_board_view_cnt")
	@ColumnDefault(value = "0")
	private Long noticeBoardViewCnt;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@Column(name="notice_board_like_cnt")
	@ColumnDefault(value = "0")
	private Long noticeBoardLikeCnt;
	
	@Column(name="notice_board_cmt_cnt")
	@ColumnDefault(value = "0")
	private Long noticeBoardCmtCnt;
	
	@JsonManagedReference
//	@JoinColumn(name="notice_board_no")
	@OneToMany(mappedBy="noticeBoard", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Fetch(value = FetchMode.SUBSELECT)
	@Singular("cmtList") // collection 타입에 붙이는 어노테이션 내부로직 파악 필요함
	private List<NoticeCommentEntity> noticeCommentList;
	
	@JsonManagedReference
	@OneToMany(mappedBy="noticeBoard", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Fetch(value = FetchMode.SUBSELECT)
	@JsonIgnore
	private List<NoticeLikeEntity> noticeLikeList;
	
	@Transient
	private NoticeCommentEntity noticeCommentEntity;
	
	@Builder
	public NoticeBoardEntity(Long noticeBoardNo, String noticeBoardTitle, String noticeBoardContent, Date noticeBoardDt, Long noticeBoardViewCnt, Long noticeBoardLikeCnt, Long noticeBoardCmtCnt, String userNickname, List<NoticeCommentEntity> noticeCommentList, List<NoticeLikeEntity> noticeLikeList) {
		this.noticeBoardNo = noticeBoardNo;
		this.noticeBoardTitle = noticeBoardTitle;
		this.noticeBoardContent = noticeBoardContent;
		this.noticeBoardDt = noticeBoardDt;
		this.noticeBoardViewCnt = noticeBoardViewCnt;
		this.noticeBoardLikeCnt = noticeBoardLikeCnt;
		this.noticeBoardCmtCnt = noticeBoardCmtCnt;
		this.userNickname = userNickname;
		this.noticeCommentList = noticeCommentList;
		this.noticeLikeList = noticeLikeList;
	}
	
	@Builder
	public NoticeBoardEntity (Long noticeBoardNo, String noticeBoardTitle, String noticeBoardContent, String userNickname){
		this.noticeBoardNo = noticeBoardNo;
		this.noticeBoardTitle = noticeBoardTitle;
		this.noticeBoardContent = noticeBoardContent;
		this.userNickname = userNickname;
	}
	
	public NoticeBoardDto toDto() {
		return NoticeBoardDto.builder()
				.noticeBoardNo(noticeBoardNo)
				.noticeBoardTitle(noticeBoardTitle)
				.noticeBoardContent(noticeBoardContent)
				.noticeBoardCmtCnt(noticeBoardCmtCnt)
				.noticeBoardDt(noticeBoardDt)
				.noticeBoardLikeCnt(noticeBoardLikeCnt)
				.noticeBoardViewCnt(noticeBoardViewCnt)
				.userNickname(userNickname)
//				.noticeCommentList(noticeCommentList)
				.build();
	}
}
