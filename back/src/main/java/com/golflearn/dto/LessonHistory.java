package com.golflearn.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"lsnLineNo, lsnChkDt"})
public class LessonHistory {
	private LessonLine lsnLine;

	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date lsnChkDt;
	private Lesson lsn;
	private Date minChkDt;
	private int cntChkDt;
}
