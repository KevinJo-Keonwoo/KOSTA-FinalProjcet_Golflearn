package com.golflearn.control;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class TestController {
	@GetMapping("/test/send")
	public String send(HttpSession session ) {
		session.setAttribute("test", "abc");
		return "SEND OK";
	}
	@GetMapping("/test/retr")
	public String retreve(HttpSession session ) {
		String result = (String)session.getAttribute("test");
		System.out.println("in retreve" + result);
		return result;
	}
}
