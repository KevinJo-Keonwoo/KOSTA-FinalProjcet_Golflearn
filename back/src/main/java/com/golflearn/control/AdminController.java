package com.golflearn.control;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.Lesson;
import com.golflearn.dto.LessonLine;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.ResultBean;
import com.golflearn.dto.UserInfo;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.service.AdminService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("admin/*")
public class AdminController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AdminService service;

	// 레슨상태별 목록보기
	@GetMapping(value = { "lesson/{lsnStatus}", "lesson/{lsnStatus}/{optCp}"})
	public ResultBean<PageBean<Lesson>> viewLesson(@PathVariable(name= "lsnStatus", required=true) Integer lsnStatus,
			@PathVariable(name= "optCp", required=true) Optional<Integer> optCp, HttpSession session) {
		ResultBean<PageBean<Lesson>> rb = new ResultBean<>();
		String loginedUserType = (String) session.getAttribute("userType");// 로그인한 유저의 유저타입가져오기
		//		String loginedUserType = "2";//테스트용
		if (loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
			rb.setStatus(0);
			rb.setMsg("관리자만 접근가능합니다");
			return rb;
		} else {
			try {
				PageBean<Lesson> pb;
				int currentPage = 1;
				if (optCp.isPresent()) {
					currentPage = optCp.get();
				}
				pb = service.searchLessonByStatus(lsnStatus, currentPage);
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
	// 레슨 승인 및 반려하기
	@PutMapping(value = "lesson/{lsnNo}")
	public ResponseEntity<String> modifyLsnStatus(@PathVariable int lsnNo, @RequestBody Lesson lesson,
			HttpSession session) {
		String loginedUserType =  (String) session.getAttribute("userType");
		//		String loginedUserType = "2";//테스트용
		if (loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
			return new ResponseEntity<>("관리자만 접근가능합니다", HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			try {
				lesson.setLsnNo(lsnNo);
				service.modifyLsnStatus(lesson);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (ModifyException e) {
				e.printStackTrace();
				return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);// 에러인 경우에도 메세지 반환
			}
		}
	}

	// 반려사유보기
	@GetMapping(value = "lesson/reject/{lsnNo}")
	public ResultBean<String> viewLsnRjtReason(@PathVariable int lsnNo, HttpSession session) {
		ResultBean<String> rb = new ResultBean<>();
		String loginedUserType = (String) session.getAttribute("userType");
		//		String loginedUserType = "2";// 테스트용
		if (loginedUserType == null || !loginedUserType.equals("2")) {// 로그인 및 관리자여부 확인
			rb.setStatus(0);
			rb.setMsg("관리자만 접근가능합니다");
		} else {
			try {
				String lsnRjtReason = service.selectLsnRjtReason(lsnNo);
				rb.setStatus(1);
				rb.setT(lsnRjtReason);
			} catch (FindException e) {
				e.printStackTrace();
				rb.setStatus(0);
				rb.setMsg(e.getMessage());
			}
		}
		return rb;
	}

	//유저 목록보기
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

	//유저 타입별 목록보기 필터
	@GetMapping(value={"usertype/{optType}", "usertype/{optType}/{optCp}"})
	public ResultBean<PageBean<UserInfo>> userTypeList(@PathVariable Optional <Integer> optType,
			@PathVariable Optional <Integer> optCp) {

		ResultBean<PageBean<UserInfo>> rb = new ResultBean<>();
		try {
			int userType;
			int currentPage;
			if(optCp.isPresent()) {
				currentPage = optCp.get();
				//					logger.error(currentPage);
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

	//유저 정보 상세보기
	@GetMapping(value={"userinfo/detail"})
	public ResultBean<UserInfo> userInfoDetail(HttpSession session, @RequestParam String userId) {
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

	//유저 레슨 목록 보기(더 수정해야함)
	@GetMapping(value="lesson/{optCp}")
	public ResultBean<PageBean<LessonLine>> userLessonList (HttpSession session, @PathVariable Optional<Integer> optCp, @RequestParam String userId, @RequestParam int userType) {
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

	//유저 검색하기
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