package com.golflearn.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"resaleCmtNo"})
public class ResaleCommentDto {
	private Long resaleCmtNo;
	private ResaleBoardDto resaleBoard;
	
	@NotEmpty(message="내용은 필수 입력값입니다.")
	private String resaleCmtContent;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date resaleCmtDt;
	private Long resaleCmtParentNo;
	private String userNickname;
	
}
