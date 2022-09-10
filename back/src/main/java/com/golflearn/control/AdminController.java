package com.golflearn.control;

import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.LessonLine;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.ResultBean;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;
import com.golflearn.service.AdminService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("admin/*")
public class AdminController {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private AdminService service;

	@Autowired
	private ServletContext sc;

	@GetMapping(value = { "userinfo", "userinfo/{optCp}" })
	public ResultBean<PageBean<UserInfo>> userInfoList(HttpSession session, @PathVariable Optional<Integer> optCp) {

		ResultBean<PageBean<UserInfo>> rb = new ResultBean<>();

		try {
			// String loginedUserType = (String) session.getAttribute("userType");// 로그인한
			// 유저의 유저타입가져오기
			String loginedUserType = "2";// 테스트용
			if (loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
				rb.setStatus(0);
				rb.setMsg("관리자만 접근가능합니다");
				return rb;
			}
			int currentPage;
			if (optCp.isPresent()) {
				currentPage = optCp.get();
			} else {
				currentPage = 1;
			}
			PageBean<UserInfo> pb = service.userInfoList(currentPage);
			rb.setStatus(1); // status 1 = 성공, 0 = 실패
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	@GetMapping(value={"usertype/{optType}", "usertype/{optType}/{optCp}"})
	public ResultBean<PageBean<UserInfo>> userTypeList(@PathVariable Optional <Integer> optType,
			@PathVariable Optional <Integer> optCp) {

		ResultBean<PageBean<UserInfo>> rb = new ResultBean<>();
		//tring loginedUserType = (String) session.getAttribute("userType");// 로그인한 유저의 유저타입가져오기
		String loginedUserType = "2";//테스트용
		if (loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
			rb.setStatus(0);
			rb.setMsg("관리자만 접근가능합니다");
			return rb;
		}else {
			try {
				int userType;
				int currentPage;
				if(optCp.isPresent()) {
					currentPage = optCp.get();
					logger.error(currentPage);
				}else {
					currentPage = 1;
				}
				if(optType.isPresent()) {
					userType = optType.get();
				}else {
					userType = 0;
				}
				PageBean<UserInfo> pb = service.userInfoByType(currentPage, userType);
				rb.setStatus(1);
				rb.setT(pb);
			}catch(FindException e)	{
				e.printStackTrace();
				rb.setStatus(0);
				rb.setMsg(e.getMessage());
			}return rb;
		}
	}

	@GetMapping(value={"userinfo/detail"})
	public ResultBean<UserInfo> userInfoDetail(@RequestParam String userId) {
		ResultBean<UserInfo> rb = new ResultBean<>();
		//		String loginedUserType = (String) session.getAttribute("userType");// 로그인한 유저의 유저타입가져오기
		String loginedUserType = "2";//테스트용
		try {
			if(loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
				rb.setStatus(0);
				rb.setMsg("관리자만 접근가능합니다");
				return rb;
			}else {
				UserInfo infoDetail = service.userInfoDetail(userId);
				rb.setStatus(1);
				rb.setT(infoDetail);
			}
		}catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}



	@GetMapping(value="lesson/{optCp}")
	public ResultBean<PageBean<LessonLine>> userLessonList (@PathVariable Optional<Integer> optCp, @RequestParam String userId, @RequestParam int userType) {
		ResultBean<PageBean<LessonLine>> rb = new ResultBean<>();
		//		String loginedUserType = (String) session.getAttribute("userType");// 로그인한 유저의 유저타입가져오기
		String loginedUserType = "2";//테스트용
		if(loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
			rb.setStatus(0);
			rb.setMsg("관리자만 접근가능합니다");
			return rb;
		}else {
			int currentPage;
			if(optCp.isPresent()) {
				currentPage = optCp.get();
			}else {
				currentPage = 1;
			}
			try {
				if(userType == 0) {
					PageBean<LessonLine> userLessonList = service.userLesson(currentPage, userType, userId);
					rb.setStatus(1);
					rb.setT(userLessonList);
				}else {
					PageBean<LessonLine> userLessonList;
				}
			} catch (FindException e) {
				e.printStackTrace();
				rb.setStatus(0);
				rb.setMsg(e.getMessage());
			}
		}
		return rb;
	}

	@GetMapping(value={"search", "search/{optWord}", "search/{optWord}/{optCp}"})
	public ResultBean<PageBean<UserInfo>> searchUser (@PathVariable Optional<String> optWord,
			@PathVariable Optional<Integer> optCp) {
		ResultBean<PageBean<UserInfo>> rb = new ResultBean<>();
		//		String loginedUserType = (String) session.getAttribute("userType");// 로그인한 유저의 유저타입가져오기
		String loginedUserType = "2";//테스트용
		if(loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
			rb.setStatus(0);
			rb.setMsg("관리자만 접근가능합니다");
			return rb;
		}else {
			try {
				PageBean<UserInfo> pb;
				String word;
				if(optWord.isPresent()) { 
					word = optWord.get();
				}else {
					word = "";
				}
				int currentPage = 1;

				if(optCp.isPresent()) {
					currentPage = optCp.get();
				}
				if("".equals(word)) {
					pb = service.userInfoList(currentPage);//문자열이 들어오지 않을 경우 모든값을 반환하게
				}else {
					pb = service.searchUser(word, currentPage);
				}
				rb.setStatus(1);
				rb.setT(pb);
			} catch (FindException e) {
				e.printStackTrace();
				rb.setStatus(0);
				rb.setMsg(e.getMessage());
			}
			return rb;
		}
	}
}


//@GetMapping(value="lesson")
//public ResultBean<PageBean<LessonLine>> userLessonList (@RequestParam String userId) {
//	ResultBean<PageBean<LessonLine>> rb = new ResultBean<>();
//	//		String loginedUserType = (String) session.getAttribute("userType");// 로그인한 유저의 유저타입가져오기
//	String loginedUserType = "2";//테스트용
//	try {
//		if(loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
//			rb.setStatus(0);
//			rb.setMsg("관리자만 접근가능합니다");
//			return rb;
//
//		}
//
//
//	}
//}