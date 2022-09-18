package com.golflearn.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@ToString
public class ResultBean<T> {
	private int status;
	private String msg;
	private T t;
	private List<T> lt;
//	private Map<K, V> mT;
	private List<Map<String, String>> sido;
	private List<Map<String, String>> sigungu;
}
