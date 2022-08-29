package com.golflearn.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.exception.FindException;
import com.golflearn.service.LessonService;

@Controller
public class LessonController {
	@Autowired
	private LessonService service;
	
	@GetMapping("viewmain")
	public Object viewMain() { //로그인 유무와 상관없이 다 볼수 있기때문에 httpSession 필요없음
		Map<String,Object> map = new HashMap<>();
		try {
			List<Lesson> lsns = service.viewMain();
			return lsns;
		} catch (FindException e) {				
			e.printStackTrace();
			map.put("status", -1);
			map.put("msg", e.getMessage());
			return map;
		}
	}
	
	@GetMapping("viewhistory")
	public Object viewHistory(HttpSession session, int lsnNo) {
		Map<String,Object> map = new HashMap<>();
		String loginedId = (String)session.getAttribute("loginInfo");
		if(loginedId == null) {
			map.put("status", 0);
			map.put("msg", "로그인하세요");
			return map;
		}else {
			try {
				List<LessonLine> lsnHistories = service.viewLessonHistory(lsnNo);
				return lsnHistories;
			} catch (FindException e) {				
				e.printStackTrace();
				map.put("status", -1);
				map.put("msg", e.getMessage());
				return map;
			}
		}
	}
	
	
}
