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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "resale_like")
@SequenceGenerator(name="resale_like_generator",
					sequenceName = "resale_like_no_seq",
					initialValue = 27,
					allocationSize = 1)
@DynamicInsert
@DynamicUpdate
public class ResaleLikeEntity {
	@Id
	@Column(name = "resale_like_no")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "resale_like_generator")	
	private Long resaleLikeNo;

	@Column(name = "resale_nickname")
	private String userNickname;
	
	@ManyToOne
	@JoinColumn(name="resale_board_no", nullable = false)
	private ResaleBoardEntity resaleBoard;
}
