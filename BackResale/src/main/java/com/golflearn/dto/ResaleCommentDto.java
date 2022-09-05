package com.golflearn.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = {"resaleCmtNo"})
public class ResaleComment {
	private Long resaleCmtNo;
//	Long resaleBoardNo;
	private ResaleBoard resaleBoard;
	private String resaleCmtContent;
	private Date resaleCmtDt;
	private Long resaleCmtParentNo;
	private String userNickname;
	
}
