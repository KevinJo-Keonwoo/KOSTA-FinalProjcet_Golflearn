package com.golflearn.control;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletContext;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.golflearn.dto.ResultBean;
import com.golflearn.dto.SmsResponse;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;
import com.golflearn.service.SmsService;
import com.golflearn.service.UserInfoService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*") // 누구든 ajax로 요청할 수 있음 (다른 포트번호도O)
@RestController // @Controller + @ResponseBody
@RequestMapping("/user/*")
@RequiredArgsConstructor
public class UserInfoController {
	private Logger logger = Logger.getLogger(getClass());
	private final SmsService smsService;
	@Autowired // 빈 객체 주입받음
	private UserInfoService service;
	
	@Autowired
	private ServletContext sc;
	
//	@GetMapping
//	public void signupStdt{
//		
//	}
	
	@PostMapping(value="find/id", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean <UserInfo> selectByUserNameAndPhone(@RequestParam String userName, @RequestParam String userPhone) throws FindException {
		ResultBean<UserInfo> rb = new ResultBean<>();
		UserInfo userInfo = new UserInfo();
		try {
			userInfo = service.selectByUserNameAndPhone(userName, userPhone);
			rb.setStatus(1);
			rb.setT(userInfo);
		}catch(FindException e) {
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}	

    //@PostMapping("sms")
   

}



