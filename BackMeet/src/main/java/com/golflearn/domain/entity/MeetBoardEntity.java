package com.golflearn.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//component어노테이션은 왜?
@Component
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of= {"meetBoardNo"})//boardNo가 같으면 같은 객체

@Entity
@Table(name= "meet_board")
@SequenceGenerator(name = "meet_board_seq_generator",
					sequenceName= "meet_board_no_seq",
					initialValue = 1,
					allocationSize = 1
					)

@DynamicInsert
@DynamicUpdate
public class MeetBoard {
	@OneToOne//1대1관계
	@JoinColumn(name= "meet_ctg_no", nullable = false)//false
	private MeetCategory meetCategory;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "meet_board_seq_generator")
	@Column(name="meet_board_no")
	private Long meetBoardNo;
	
	@Column(name="user_nickname")
	private String userNickname;
	
	@Column(name="meet_board_title")
	private String meetBoardTitle;
	
	@Column(name="meet_board_content")
	private String meetBoardContent;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	@Column(name="meet_board_dt")
	@ColumnDefault(value = "SYSDATE") 
	private Date meetBoardDt;
	
	@Column(name="meet_board_status")
	@ColumnDefault(value = "0")//모집중
	private Long meetBoardStatus;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul") 
	@Column(name="meet_board_meet_dt")
	private Date meetBoardMeetDt;
	
	@Column(name="meet_board_cur_cnt")
	private Long meetBaordCurCnt;
	
	@Column(name="meet_board_max_cnt")
	private Long meetBoardMaxCnt;
	
	@Column(name="meet_board_location")
	private String meetBoardLocation;
	

}
