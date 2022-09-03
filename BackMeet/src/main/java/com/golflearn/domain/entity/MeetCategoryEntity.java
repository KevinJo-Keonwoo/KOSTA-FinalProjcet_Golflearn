package com.golflearn.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Getter @Setter
@EqualsAndHashCode(of = {"meetCtgNo"})

@Entity
@Table(name= "meet_category")

@DynamicInsert
@DynamicUpdate
public class MeetCategory {
	
	@Id
	@Column(name="meet_ctg_no")
	private Long meetCtgNo;
	
	@Column(name="meet_ctg_title")
	private String meetCtgTitle;
}
