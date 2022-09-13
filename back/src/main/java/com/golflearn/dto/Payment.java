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
@EqualsAndHashCode(of = {"paymentNo"})
public class Payment {
	private int paymentNo;
	private String paymentMethod;
	
	@JsonFormat(pattern = "yy/MM/dd", timezone = "Asia/Seoul")
	private Date paymentDt;
}
