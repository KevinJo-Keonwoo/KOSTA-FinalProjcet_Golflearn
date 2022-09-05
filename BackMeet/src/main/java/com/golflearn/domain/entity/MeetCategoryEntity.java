package com.golflearn.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"meetCtgNo"})

@Entity
@Table(name= "meet_category")

@DynamicInsert
@DynamicUpdate
public class MeetCategoryEntity {
	
	@Id
	@Column(name="meet_ctg_no")
	private Long meetCtgNo;
	
	@Column(name="meet_ctg_title")
	private String meetCtgTitle;
}
