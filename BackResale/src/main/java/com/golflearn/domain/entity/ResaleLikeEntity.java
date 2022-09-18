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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter 
@Entity
@Table(name = "resale_like")
@SequenceGenerator(name="resale_like_generator",
					sequenceName = "resale_like_no_seq",
					initialValue = 1,
					allocationSize = 1)
@DynamicInsert
@DynamicUpdate
public class ResaleLikeEntity {
	@Id
	@Column(name = "resale_like_no")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
					generator = "resale_like_generator")	
	private Long resaleLikeNo;

	@Column(name = "user_nickname")
	private String userNickname;

	//Board쪽에서 OneToMany로 가지고 있음
	@JsonBackReference //연관관계의 주인 Entity 에 선언. 직렬화 되지 않도록 수행
	@ManyToOne (optional = false) //(fetch=FetchType.LAZY)
	@JoinColumn(name = "resale_board_no" )//,nullable = false)
	private ResaleBoardEntity resaleBoard;
//	@JoinColumn(name="resale_board_no", nullable = false)
}