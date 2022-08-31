package com.golflearn.control;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.FindException;
import com.golflearn.service.LessonLineService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("mypage/*")
public class LessonLineController {
	@Autowired
	private LessonLineService service;
	
	//front딴에서 구별해버리기 
//	public int userType(String userId) {
//		return 0;
//	}	
	
	@GetMapping(value = "student") //userId로 가는것이 맞나? 
	public ResultBean<LessonLine> myPage(HttpSession session) {
		ResultBean<LessonLine> rb = new ResultBean<>();
		String userId = (String)session.getAttribute("loginInfo");
		try {
			List<LessonLine> lessonLine = service.myLessonList(userId);
			rb.setStatus(1);
			rb.setLt(lessonLine);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	@GetMapping(value = "pro")
	public ResultBean<Lesson> myProPage(HttpSession session){
		ResultBean<Lesson> rb = new ResultBean<>();
		String userId = (String)session.getAttribute("loginInfo");
		try {
			List<Lesson> lesson = service.proLessonList(userId);
			rb.setStatus(1);
			rb.setLt(lesson);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	//유저타입세션으로 받아오기 
//	public int userType(String userId) {
//		return 0;
//	}
}
