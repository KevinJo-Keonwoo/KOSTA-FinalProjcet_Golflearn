package com.golflearn.dto;

import java.util.Date;
import java.util.List;

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
	private Lesson lsn;
//	private List<LessonLine> lsnLines;
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date lsnChkDt;
	private Date minChkDt;
	private int cntChkDt;
}
