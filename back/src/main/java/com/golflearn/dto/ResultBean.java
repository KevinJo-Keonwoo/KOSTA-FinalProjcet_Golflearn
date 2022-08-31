package com.golflearn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor //여기에 필드에 쓴 모든생성자만 만들어줌
@AllArgsConstructor //기본 생성자를 만들어줌
@Setter @Getter
@ToString
public class ResultBean<T> {
	private int status;
	private String msg;
	private T t;
}
