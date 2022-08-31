package com.golflearn.domain;

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
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Component
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EqualsAndHashCode(of= {"meetMbNo"})//boardNo가 같으면 같은 객체

@Entity
@Table(name= "meet_member")
@SequenceGenerator(name = "meet_member_seq_generator",
					sequenceName= "meet_member_no_seq",
					initialValue = 1,
					allocationSize = 1
					)

@DynamicInsert
@DynamicUpdate
public class MeetMember {

	//단방향 - member는 board가 필요X, 그 반대임
	//여러 meetMember - 하나 meetboard
	@ManyToOne
	@JoinColumn(name = "meet_board_no")
	private MeetBoard meetBoard;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
	generator = "meet_member_seq_generator")
	@Column(name = "meet_mb_no")
	private Long meetMbNo;
	
	@Column(name = "user_nickname")
	private String userNickname;
}
