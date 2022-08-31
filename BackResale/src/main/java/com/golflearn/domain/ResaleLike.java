package com.golflearn.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "resale_like")
public class ResaleLike {
	private Long resaleLikeNo;
//	private Long resaleBoardNo;
	private ResaleBoard resaleBoard;
	private String userNickname;
}
