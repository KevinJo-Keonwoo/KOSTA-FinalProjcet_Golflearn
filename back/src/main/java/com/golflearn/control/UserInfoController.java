package com.golflearn.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.golflearn.service.UserInfoService;

@Controller
public class UserInfoController {

	@Autowired // 빈 객체 주입받음
	private UserInfoService service;
	
	
}
