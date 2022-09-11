package com.golflearn.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"noticeLikeNo"})
@Table(name= "notice_like")
@SequenceGenerator(name = "noticelike_seq_generator",
					sequenceName= "notice_like_seq",
					initialValue = 1,
					allocationSize = 1
					)
@DynamicInsert
@DynamicUpdate
public class NoticeLike {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "noticelike_seq_generator")
	@Column(name="notice_like_no")
	private Long noticeLikeNo;
	
	@ManyToOne
	@JoinColumn(name="notice_board_no")
	@NonNull
	private NoticeBoard noticeBoard;
	
	@Column(name="user_nickname")
	@NonNull
	private String userNickname;
	
}
