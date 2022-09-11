package com.golflearn.domain.entity;

import javax.persistence.CascadeType;
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
import lombok.Builder;
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
					sequenceName= "notice_like_no_seq",
					initialValue = 1,
					allocationSize = 1
					)
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class NoticeLikeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "noticelike_seq_generator")
	@Column(name="notice_like_no")
	private Long noticeLikeNo;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name="notice_board_no")
	private NoticeBoardEntity noticeBoard;
	
	@Column(name="user_nickname")
	private String userNickname;
	
}
