package com.golflearn.control;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.golflearn.dto.MeetBoardDto;
import com.golflearn.dto.MeetMemberDto;
import com.golflearn.dto.PageBean;
import com.golflearn.dto.ResultBean;
import com.golflearn.exception.AddException;
import com.golflearn.exception.FindException;
import com.golflearn.exception.ModifyException;
import com.golflearn.exception.RemoveException;
import com.golflearn.service.MeetBoardService;

@CrossOrigin(origins = "*")//모든포트에서 접속가능 //메서드마다 각각 설정가능
@RestController
@RequestMapping("board/*")
public class MeetBoardController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MeetBoardService service;
	
	//게시글 목록보기 -테스트완료
	@GetMapping(value = { "list", "list/{optCp}" }) // PathBariable이 전달될 수도, 안될수도 있음
	public ResultBean<PageBean<MeetBoardDto>> list(@PathVariable Optional<Integer> optCp) {// PathVariable이 전달되지 않은 경우 null인지 아닌지 비교 가능케 함

		ResultBean<PageBean<MeetBoardDto>> rb = new ResultBean<>();
		try {
			int currentPage;
			if (optCp.isPresent()) {
				currentPage = optCp.get();
			} else {
				currentPage = 1;
			}
			PageBean<MeetBoardDto> pb = service.meetBoardList(currentPage);
			rb.setStatus(1);
			rb.setT(pb);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	
	//모집상태에 따른 게시글 목록보기 -테스트완료
	@GetMapping(value = { "filter", "filter/{optStatus}", "filter/{optStatus}/{optCp}" })
	public ResultBean<PageBean<MeetBoardDto>> filter(@PathVariable Optional<Integer> optCp, @PathVariable Optional<Long> optStatus) {
		ResultBean<PageBean<MeetBoardDto>> rb = new ResultBean<>();
		try {
			PageBean<MeetBoardDto> pb;
			Long meetBoardStatus ;
			
			if (optStatus.isPresent()) {
				meetBoardStatus = optStatus.get();
			} else {
				meetBoardStatus = null;
			}
			int currentPage = 1;
			if (optCp.isPresent()) {
				currentPage = optCp.get();
			}
			if (meetBoardStatus == null) {//모집상태필터를 선택하지 않은 경우
				pb = service.meetBoardList(currentPage);
			} else {//선택한 경우
				pb = service.filterMeetBoard(meetBoardStatus, currentPage);
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

	// 게시글목록에서 제목 검색하기-테스트완료
	// GET /meet/search/검색어/페이지번호 
	// GET /meet/search/검색어 
	@GetMapping(value = { "search", "search/{optWord}", "search/{optWord}/{optCp}" })
	public ResultBean<PageBean<MeetBoardDto>> search(@PathVariable Optional<Integer> optCp, @PathVariable Optional<String> optWord) {
		ResultBean<PageBean<MeetBoardDto>> rb = new ResultBean<>();
		try {
			PageBean<MeetBoardDto> pb;
			String word;
			if (optWord.isPresent()) {//검색어 확인
				word = optWord.get();
			} else {
				word = "";
			}
			int currentPage = 1;
			if (optCp.isPresent()) {//선택한 페이지 확인
				currentPage = optCp.get();
			}
			if ("".equals(word)) {//검색어 없는 경우
				pb = service.meetBoardList(currentPage);
			} else {//있는경우
				pb = service.searchMeetBoard(word, currentPage);

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
	
	//게시글 상세보기 - 테스트완료
	@GetMapping("{meetBoardNo}")
	public ResultBean<MeetBoardDto> viewBoard(@PathVariable long meetBoardNo) {
		ResultBean<MeetBoardDto> rb = new ResultBean<>();
		try {
			MeetBoardDto m = service.viewMeetBoard(meetBoardNo);
			rb.setStatus(1);
			rb.setT(m);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	
	//게시글 작성하기-테스트완료
	@PostMapping("write")
	public ResponseEntity<Object> write(@RequestParam Long meetCtgNo, @RequestBody MeetBoardDto m){
//		logger.error("title: " + m.getMeetBoardTitle() );
		
		try {//모집유형리스트 드롭다운에 불러오기
			service.meetCategoryList();
		} catch (FindException e1) {
			e1.printStackTrace();
		}
		
		if (m.getMeetBoardTitle() == null || m.getMeetBoardTitle().equals("") || m.getMeetBoardContent() == null
				|| m.getMeetBoardLocation() == null || m.getMeetBoardMaxCnt() == null || m.getMeetBoardMeetDt() == null
				|| meetCtgNo == null) {
			return new ResponseEntity<>("항목을 모두 입력하세요", HttpStatus.BAD_REQUEST);
		}
		
//		String loginedNickname = (String)session.getAttribute("loginedNickname");//front에서 로그인여부 확인?
		//---로그인대신할 샘플데이터---
		String loginedNickname = "케빈";
		m.setUserNickname(loginedNickname);
		try {
			service.writeMeetBoard(m, meetCtgNo);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//게시글 삭제하기 -테스트완료
	@DeleteMapping(value = "{meetBoardNo}")
	public ResponseEntity<String> remove(@PathVariable Long meetBoardNo) {
		try {
			service.removeMeetBoard(meetBoardNo);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RemoveException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//게시글 수정하기-테스트완료
	@PutMapping(value = "{meetBoardNo}")
	public ResponseEntity<Object> modify(@PathVariable long meetBoardNo, @RequestBody MeetBoardDto m) {
		
		try {
			if (m.getMeetBoardContent() == null || m.getMeetBoardContent().equals("") || m.getMeetBoardTitle() == null
				|| m.getMeetBoardLocation() == null || m.getMeetBoardMaxCnt() == null) {
				return new ResponseEntity<>("항목을 모두 입력하세요", HttpStatus.BAD_REQUEST);
			}
			m.setMeetBoardNo(meetBoardNo);
			service.modifyMeetBoard(m);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ModifyException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 글 작성자가 모임글의 상태를 수정한다 -테스트완료
	@PutMapping(value = "update/{meetBoardNo}/{meetBoardStatus}")	
	public ResponseEntity<Object> modifySatus(@PathVariable Long meetBoardNo, @PathVariable Long meetBoardStatus) {
		//글작성자 유효성검사 필요
		try {
			service.modifyStatus(meetBoardNo, meetBoardStatus);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ModifyException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//모임에 참여한다 --테스트완료
	//모임글 참여가 모임상세보기에서 이루어지니까 상세보기랑 한 컨트롤러인가? //유효성검사 서비스단에서 하는것이 맞나?
	@PostMapping(value = "add")	
	public ResponseEntity<Object> addMember(@RequestBody MeetMemberDto m) {
		
//		String loginedNickname = (String)session.getAttribute("loginedNickname");
		//---로그인대신할 샘플데이터---
		String loginedNickname = "용오";
		m.setUserNickname(loginedNickname);
		try {
			service.addMember(m);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (AddException e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//모임에서 나간다
	


	
}
