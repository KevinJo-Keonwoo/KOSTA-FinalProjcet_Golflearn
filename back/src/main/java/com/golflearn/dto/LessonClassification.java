package com.golflearn.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
//@EqualsAndHashCode(of = {"clubNo"})
public class LessonClassification {
	private int clubNo;
	
	private Lesson lesson;
}
