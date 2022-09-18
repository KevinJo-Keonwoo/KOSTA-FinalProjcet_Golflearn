package com.golflearn.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.FindException;
import com.golflearn.domain.LessonOracleRepository;
import com.golflearn.domain.LessonRepository;
import com.golflearn.service.LessonService;
import com.golflearn.service.OpenApi;
import com.golflearn.service.SmsService;
import com.golflearn.service.UserInfoService;
import com.golflearn.service.ViewMainService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*") // 누구든 ajax로 요청할 수 있음 (다른 포트번호도O)
@RestController // @Controller + @ResponseBody
@RequestMapping("/main") // 각 메서드 앞에 붙여도됨
@RequiredArgsConstructor
public class ViewMainController {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired // 빈 객체 주입받음
	private ViewMainService service;

	@Autowired 
	private ServletContext sc;
	
	@GetMapping  
	public ResultBean<Lesson> viewmain() {
		ResultBean<Lesson> rb = new ResultBean<>();
		
		try {
			List<Lesson> lessons = service.viewLesson();
			rb.setStatus(1);
			rb.setLt(lessons);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	
	@GetMapping(value = {"/{locNo}"})
	public ResultBean<Lesson> viewFilteredLesson(@PathVariable Optional<String> locNo) {
		ResultBean<Lesson> rb = new ResultBean<>();
		logger.error("--------" + locNo.get());
		String [] arr = locNo.get().split("&");
		ArrayList<Integer>  arr1 = new ArrayList<Integer> ();

		String [] arr2 = null;
//		
		for (int i =0; i < arr.length; i++) {
			arr1.add(Integer.parseInt(arr[i].split("=")[1]));
		}

		int[] array = new int[arr1.size()];
		int size=0;
		for(int temp : arr1) {
			array[size++] = temp;
		}
		for (int a : array) {
			logger.error("--------" + a);
		}
		
		
//		for
		
//		logger.error("--------" + locNo.get().split("&")[0].split("=")[1]);
//		for(int i = 0; i < locNo.size(); i++)
//			locNoVal[i] = locNo.get(i);
//
		try {
			List<Lesson> lessons = service.filterLesson(array);
			rb.setStatus(1);
			rb.setLt(lessons);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	

//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setContentType("application/json;charset=UTF-8");	
//		PrintWriter out = response.getWriter();
//
//		HttpSession session = request.getSession();
//		
//		String userType = (String) session.getAttribute("userType");
//		String logined = (String) session.getAttribute("logininfo");
//		System.out.println(logined);
//		String result = "";
//		LessonRepository repo = new LessonOracleRepository();
//		try {
//			// json 컨텐츠를 java 객체로 역직렬화하거나 json으로 직렬화할때 사용하는 라이브러리임
//			ObjectMapper mapper = new ObjectMapper(); 
//			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
//			
//			Map<String, Object> map = new HashMap<>();
//			List<Lesson> lsnList = repo.selectAll();
//			// 광역시들을 메인에 보여주는 코드
//			OpenApi api = new OpenApi();
//			List sidoList = new ArrayList();
//
//			try {
//				map.put("lsns", lsnList);
//				map.put("sido", api.sidoApi());
//				map.put("userType", userType);
//				map.put("logined", logined);
//				request.setAttribute("lsns", lsnList);
//				request.setAttribute("sido", api.sidoApi());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			// String타입으로 변환
//			String jsonValue = mapper.writeValueAsString(map);
//
// 			result = mapper.writeValueAsString(map);
//			
//			out.print(result);
//		} catch (FindException e) {
//			e.printStackTrace();
//			Map<String, Object> map = new HashMap<>();
//
//			map.put("msg", e.getMessage());
//			ObjectMapper mapper = new ObjectMapper();
//			result = mapper.writeValueAsString(map);
//			out.print(result);
//		}
//		
//	}

}