package com.golflearn.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.FindException;
import com.golflearn.service.LessonService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("lesson/*")
public class LessonController {
	@Autowired
	private LessonService service;
	
	@GetMapping(value= {""})
	public ResultBean<Lesson> list(@PathVariable Optional<Integer> optCp) { //로그인 유무와 상관없이 다 볼수 있기때문에 httpSession 필요없음
		ResultBean<Lesson> rb = new ResultBean<>();
		try {
			List<Lesson> lessons = service.viewMain();
			rb.setStatus(1);
			rb.setLt(lessons);
			return rb;
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(-1);
			rb.setMsg(e.getMessage());
			return rb;
		}
	}

	@GetMapping(value = {"lesson", "lesson/{optCp}"}) // 프로의 레슨내역에서 레슨번호에 대한 히스토리
	public ResultBean<LessonLine> viewHistory(@PathVariable int optCp, HttpSession session) {
		ResultBean<LessonLine> rb = new ResultBean<>();
		// 로그인 여부를 받아와야한다 HttpSession?
		String loginedId = (String)session.getAttribute("loginInfo");
		if(loginedId == null) {
			rb.setStatus(0);
			rb.setMsg("로그인하세요");
			return rb;
		}else {
			try {
				List<LessonLine> lsnHistories = service.viewLessonHistory(optCp);
				rb.setStatus(1);
				rb.setLt(lsnHistories);
				return rb;
			} catch (FindException e) {				
				e.printStackTrace();
				rb.setStatus(-1);
				rb.setMsg(e.getMessage());
				return rb;
			}
		}
	}
	
	
}
