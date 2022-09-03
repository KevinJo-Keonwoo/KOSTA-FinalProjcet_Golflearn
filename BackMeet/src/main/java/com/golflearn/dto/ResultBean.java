package com.golflearn.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class ResultBean<T> {
	private int status;
	private String msg;
	private T t;
	private List<T> lt;
}
