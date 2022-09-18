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

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golflearn.dto.Lesson;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.FindException;
import com.golflearn.service.LessonService;
import com.golflearn.service.OpenApi;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*") // 누구든 ajax로 요청할 수 있음 (다른 포트번호도O)
@RestController // @Controller + @ResponseBody
@RequestMapping("/seeksidosigu") // 각 메서드 앞에 붙여도됨
@RequiredArgsConstructor
public class SeekSidoSiguController {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired // 빈 객체 주입받음
	private OpenApi service;

	@Autowired 
	private ServletContext sc;
	
	@GetMapping(value={"","/{sido}"})
	public ResultBean<List> sidogu(@PathVariable Optional<String> sido) throws Exception {
		ResultBean<List> rb = new ResultBean<>();
		String word = ""; 
		if(sido.isPresent()) {
			word = sido.get();
		} else { 
			word = "";
		}
		if("".equals(word)) {
			rb.setSido(service.sidoApi());
		} else {
			rb.setSigungu(service.siguApi(sido));
		} 
//		try {
//			rb.setStatus(1);
//			rb.setSido(service.sidoApi());
//			rb.setSigungu(service.siguApi(sido));
//		} catch (FindException e) {
//			e.printStackTrace();
//			rb.setStatus(0);
//			rb.setMsg(e.getMessage());
//		}
		return rb;
	}
	
	
	

//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.setContentType("application/json;charset=UTF-8");	
//		PrintWriter out = response.getWriter();
//		
//		String sidoVal = request.getParameter("sido");
//		
//		String result = "";
//		OpenApi api = new OpenApi();
//		List sidoList = new ArrayList();
//		
//		try {
//			ObjectMapper mapper = new ObjectMapper(); 
//			Map<String, Object> map = new HashMap<>();
//			map.put("sido", api.sidoApi());
//			map.put("sigungu", api.siguApi(sidoVal));
//			request.setAttribute("sido", api.sidoApi());
//			request.setAttribute("sigungu", api.siguApi(sidoVal));
//			String jsonValue = mapper.writeValueAsString(map);
////		System.out.println("jsonValue :"+ jsonValue);
//			result = mapper.writeValueAsString(map);
//			out.print(result);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			Map<String, Object> map = new HashMap<>();
//
//			map.put("msg", e.getMessage());
//			ObjectMapper mapper = new ObjectMapper();
//			result = mapper.writeValueAsString(map);
////			System.out.println("result: " + result);
//			out.print(result);
//		}
//	}

}
