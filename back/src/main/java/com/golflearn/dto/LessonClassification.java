package com.golflearn.dto;

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
//@EqualsAndHashCode(of = {"clubNo"})
public class LessonClassification {
	private int clubNo;
	
	private Lesson lesson;
}
