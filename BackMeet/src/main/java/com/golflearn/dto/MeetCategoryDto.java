package com.golflearn.dto;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@DynamicInsert
@DynamicUpdate
public class MeetCategoryDto {

	private Long meetCtgNo;
	
	private String meetCtgTitle;

}
